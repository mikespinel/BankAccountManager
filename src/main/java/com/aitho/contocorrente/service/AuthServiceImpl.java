package com.aitho.contocorrente.service;

import com.aitho.contocorrente.dto.request.LoginRequestDto;
import com.aitho.contocorrente.dto.request.SignupRequestDto;
import com.aitho.contocorrente.dto.request.TokenRefreshRequestDto;
import com.aitho.contocorrente.dto.response.CustomerResponseDto;
import com.aitho.contocorrente.dto.response.JwtResponseDto;
import com.aitho.contocorrente.dto.response.TokenRefreshResponseDto;
import com.aitho.contocorrente.enums.RoleEnum;
import com.aitho.contocorrente.exception.RegisterCustomerException;
import com.aitho.contocorrente.mapper.CustomerMapper;
import com.aitho.contocorrente.model.Customer;
import com.aitho.contocorrente.model.RefreshToken;
import com.aitho.contocorrente.model.Role;
import com.aitho.contocorrente.repository.CustomerRepository;
import com.aitho.contocorrente.repository.RefreshTokenRepository;
import com.aitho.contocorrente.repository.RoleRepository;
import com.aitho.contocorrente.security.UserDetailsImpl;
import com.aitho.contocorrente.util.JwtUtil;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.ParseException;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;
    private final CustomerRepository customerRepository;
    private final CustomerService customerService;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CustomerMapper customerMapper;

    private static final String ROLE_NOT_FOUND = "Error: Role is not found.";

    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            CustomerRepository customerRepository,
            CustomerService customerService,
            RoleRepository roleRepository,
            RefreshTokenRepository refreshTokenRepository, CustomerMapper customerMapper
    ) {
        this.authenticationManager = authenticationManager;
        this.encoder = passwordEncoder;
        this.customerRepository = customerRepository;
        this.customerService = customerService;
        this.roleRepository = roleRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    @Transactional
    public JwtResponseDto signin(LoginRequestDto loginRequestDto, HttpServletRequest request) {
        log.info("Trying sign in for username {}", loginRequestDto.getUsername());
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Customer customer = customerService.getByUsername(userDetails.getUsername());

        String jwt = JwtUtil.createAccessToken(userDetails, request.getRequestURL().toString());
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        Map<String, Object> refreshTokenMap = JwtUtil.createRefreshToken(userDetails.getUsername());

        RefreshToken refreshToken = RefreshToken.builder()
                .customer(customer)
                .token((String) refreshTokenMap.get("refreshToken"))
                .expiryDate((Instant) refreshTokenMap.get("expiryDate"))
                .build();

        log.debug("saving refresh token : {}", refreshToken);
        refreshTokenRepository.save(refreshToken);

        return JwtResponseDto.builder()
                .token(jwt)
                .refreshToken((String) refreshTokenMap.get("refreshToken"))
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .roles(roles)
                .build();
    }

    @Override
    @Transactional
    public CustomerResponseDto registerCustomer(@Valid @RequestBody SignupRequestDto signUpRequestDto) {
        log.info("registering new customer with username {}", signUpRequestDto.getUsername());

        if (Boolean.TRUE.equals(customerRepository.existsByUsername(signUpRequestDto.getUsername())))
            throw new RegisterCustomerException("Error: Username is already taken!");

        if (Boolean.TRUE.equals(customerRepository.existsByEmail(signUpRequestDto.getEmail())))
            throw new RegisterCustomerException("Error: Email is already in use!");

        if (Boolean.TRUE.equals(customerRepository.existsByTaxCode(signUpRequestDto.getTaxCode())))
            throw new RegisterCustomerException("Error: TaxCode is already in use!");

        Customer customer = customerMapper.signUpRequestDtoToCustomer(signUpRequestDto);
        customer.setPassword(encoder.encode(signUpRequestDto.getPassword()));
        customer.setRoles(getRoles(signUpRequestDto.getRoles()));

        return customerMapper.toResponseDto(customerRepository.save(customer));

    }

    private Set<Role> getRoles(Set<String> strRoles) {
        log.debug("getting roles from list {}", strRoles);
        Set<Role> roles = new HashSet<>();
        if (strRoles == null) {
            Role userRole = roleRepository.findByName(RoleEnum.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(RoleEnum.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND));
                        roles.add(adminRole);
                        break;

                    case "mod":
                        Role modRole = roleRepository.findByName(RoleEnum.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND));
                        roles.add(modRole);
                        break;

                    default:
                        Role userRole = roleRepository.findByName(RoleEnum.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND));
                        roles.add(userRole);
                }
            });
        }
        log.debug("roles founded {}", roles.stream().map(Role::getName));
        return roles;
    }

    @Override
    @Transactional
    public TokenRefreshResponseDto refreshToken(TokenRefreshRequestDto request, String issuer) throws BadJOSEException, ParseException, JOSEException {
        log.info("refreshing token with refreshToken {}", request.getRefreshToken());
        String refreshToken = request.getRefreshToken();

        if (!refreshTokenRepository.findByToken(refreshToken).isPresent())
            throw new EntityNotFoundException("Token not found in DB!");

        JwtUtil.verifyExpiration(refreshToken);
        UsernamePasswordAuthenticationToken authenticationToken = JwtUtil.parseToken(refreshToken);
        String username = authenticationToken.getName();
        Customer customer = customerService.getByUsername(username);

        List<String> roles = customer.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toList());
        String accessToken = JwtUtil.createAccessToken(username, issuer, roles);
        log.debug("created access token {}", accessToken);

        return new TokenRefreshResponseDto(accessToken, refreshToken);
    }

    @Override
    @Transactional
    public int deleteRefreshTokenByUsername(String username) {
        log.info("deleting refreshToken for username {}", username);
        Customer customer = customerService.getByUsername(username);
        return refreshTokenRepository.deleteByCustomer(customer);
    }
}
