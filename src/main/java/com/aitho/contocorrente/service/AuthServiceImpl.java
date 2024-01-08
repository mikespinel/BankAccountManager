package com.aitho.contocorrente.service;

import com.aitho.contocorrente.dto.request.LoginRequest;
import com.aitho.contocorrente.dto.request.SignupRequest;
import com.aitho.contocorrente.dto.request.TokenRefreshRequest;
import com.aitho.contocorrente.dto.response.JwtResponse;
import com.aitho.contocorrente.dto.response.MessageResponse;
import com.aitho.contocorrente.dto.response.TokenRefreshResponse;
import com.aitho.contocorrente.exception.TokenRefreshException;
import com.aitho.contocorrente.model.Customer;
import com.aitho.contocorrente.model.RefreshToken;
import com.aitho.contocorrente.model.Role;
import com.aitho.contocorrente.enums.RoleEnum;
import com.aitho.contocorrente.repository.CustomerRepository;
import com.aitho.contocorrente.repository.RefreshTokenRepository;
import com.aitho.contocorrente.repository.RoleRepository;
import com.aitho.contocorrente.security.UserDetailsImpl;
import com.aitho.contocorrente.util.JwtUtil;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;
    private final CustomerRepository customerRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            CustomerRepository customerRepository,
            RoleRepository roleRepository,
            RefreshTokenRepository refreshTokenRepository
    ) {
        this.authenticationManager = authenticationManager;
        this.encoder = passwordEncoder;
        this.customerRepository = customerRepository;
        this.roleRepository = roleRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public JwtResponse signin(LoginRequest loginRequest, HttpServletRequest request) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Optional <Customer> customer = customerRepository.findByUsername(userDetails.getUsername());
        if(customer.isPresent()){
            String jwt = JwtUtil.createAccessToken(userDetails, request.getRequestURL().toString());

            List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            Map<String, Object> refreshTokenMap = JwtUtil.createRefreshToken(userDetails.getUsername());

            RefreshToken refreshToken = new RefreshToken(1L, customer.get(), (String) refreshTokenMap.get("refreshToken"), (Instant) refreshTokenMap.get("expiryDate"));
            refreshTokenRepository.save(refreshToken);
            return new JwtResponse(jwt, (String) refreshTokenMap.get("refreshToken"),
                    userDetails.getUsername(), userDetails.getEmail(), roles);
        }else{
            throw new EntityNotFoundException("Impossibile trovare l'utente specificato");
        }

    }

    @Override
    public ResponseEntity<?> registerCustomer(@Valid @RequestBody SignupRequest signUpRequest) {
        if (Boolean.TRUE.equals(customerRepository.existsByUsername(signUpRequest.getUsername()))) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (Boolean.TRUE.equals(customerRepository.existsByEmail(signUpRequest.getEmail()))) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        Customer customer = new Customer(
                signUpRequest.getFirstName(),
                signUpRequest.getLastName(),
                signUpRequest.getTaxCode(),
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(RoleEnum.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(RoleEnum.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(RoleEnum.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(RoleEnum.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        customer.setRoles(roles);
        customerRepository.save(customer);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @Override
    public ResponseEntity<?> refreshToken(TokenRefreshRequest request, String issuer) throws BadJOSEException, ParseException, JOSEException {
        String refreshToken = request.getRefreshToken();
        if(refreshTokenRepository.findByToken(refreshToken).isPresent()){
            JwtUtil.verifyExpiration(refreshToken);
            UsernamePasswordAuthenticationToken authenticationToken = JwtUtil.parseToken(refreshToken);
            String username = authenticationToken.getName();
            Optional<Customer> customer = customerRepository.findByUsername(username);
            if(customer.isPresent()){
                List<String> roles = customer.get().getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toList());
                String accessToken = JwtUtil.createAccessToken(username, issuer, roles);

                return ResponseEntity.ok(new TokenRefreshResponse(accessToken, refreshToken));
            }else{
                throw new TokenRefreshException(refreshToken, "User not found");
            }
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @Transactional
    @Override
    public int deleteRefreshTokenByUsername(String username) {
        Optional<Customer> customer = customerRepository.findByUsername(username);
        if(customer.isPresent()){
            return refreshTokenRepository.deleteByCustomer(customer.get());
        }else{
            throw new EntityNotFoundException("Impossibile trovare l'utente specificato");
        }
    }
}
