package com.aitho.contocorrente.service;

import com.aitho.contocorrente.dto.request.LoginRequest;
import com.aitho.contocorrente.dto.request.SignupRequest;
import com.aitho.contocorrente.dto.request.TokenRefreshRequest;
import com.aitho.contocorrente.dto.response.JwtResponse;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

public interface AuthService {
    JwtResponse signin(LoginRequest loginRequest, HttpServletRequest request);

    ResponseEntity<?> registerCustomer(SignupRequest signUpRequest);

    ResponseEntity<?> refreshToken(TokenRefreshRequest request, String issuer) throws BadJOSEException, ParseException, JOSEException;

    @Transactional
    int deleteRefreshTokenByUsername(String username);
}
