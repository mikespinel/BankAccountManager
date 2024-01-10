package com.aitho.contocorrente.service;

import com.aitho.contocorrente.dto.request.LoginRequestDto;
import com.aitho.contocorrente.dto.request.SignupRequestDto;
import com.aitho.contocorrente.dto.request.TokenRefreshRequestDto;
import com.aitho.contocorrente.dto.response.CustomerResponseDto;
import com.aitho.contocorrente.dto.response.JwtResponseDto;
import com.aitho.contocorrente.dto.response.TokenRefreshResponseDto;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

public interface AuthService {
    JwtResponseDto signin(LoginRequestDto loginRequestDto, HttpServletRequest request);

    CustomerResponseDto registerCustomer(SignupRequestDto signUpRequestDto);

    TokenRefreshResponseDto refreshToken(TokenRefreshRequestDto request, String issuer) throws BadJOSEException, ParseException, JOSEException;

    int deleteRefreshTokenByUsername(String username);
}
