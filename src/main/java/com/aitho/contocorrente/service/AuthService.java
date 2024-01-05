package com.aitho.contocorrente.service;

import com.aitho.contocorrente.dto.request.LoginRequest;
import com.aitho.contocorrente.dto.response.JwtResponse;

import javax.servlet.http.HttpServletRequest;

public interface AuthService {
    JwtResponse signin(LoginRequest loginRequest, HttpServletRequest request);
}
