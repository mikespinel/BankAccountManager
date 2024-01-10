package com.aitho.contocorrente.controller;

import com.aitho.contocorrente.dto.request.LoginRequestDto;
import com.aitho.contocorrente.dto.request.SignupRequestDto;
import com.aitho.contocorrente.dto.request.TokenRefreshRequestDto;
import com.aitho.contocorrente.dto.response.CustomerResponseDto;
import com.aitho.contocorrente.dto.response.JwtResponseDto;
import com.aitho.contocorrente.dto.response.MessageResponseDto;
import com.aitho.contocorrente.dto.response.TokenRefreshResponseDto;
import com.aitho.contocorrente.service.AuthService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.ParseException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtResponseDto> authenticateUser(@Valid @RequestBody LoginRequestDto loginRequestDto, HttpServletRequest httpServletRequest) {
        JwtResponseDto response = service.signin(loginRequestDto, httpServletRequest);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<CustomerResponseDto> registerUser(@Valid @RequestBody SignupRequestDto signUpRequestDto) {
        CustomerResponseDto responseDto = service.registerCustomer(signUpRequestDto);
        return ResponseEntity.ok().body(responseDto);
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<TokenRefreshResponseDto> refreshtoken(@Valid @RequestBody TokenRefreshRequestDto tokenRefreshRequestDto, HttpServletRequest httpServletRequest) throws BadJOSEException, ParseException, JOSEException {
        TokenRefreshResponseDto responseDto = service.refreshToken(tokenRefreshRequestDto, httpServletRequest.getRequestURL().toString());
        return ResponseEntity.ok().body(responseDto);
    }

    @PostMapping("/signout")
    public ResponseEntity<MessageResponseDto> logoutUser(HttpServletRequest request) {
        service.deleteRefreshTokenByUsername(request.getUserPrincipal().getName());
        return ResponseEntity.ok(new MessageResponseDto("Log out successful!"));

    }
}
