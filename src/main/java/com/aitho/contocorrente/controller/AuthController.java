package com.aitho.contocorrente.controller;

import com.aitho.contocorrente.dto.request.LoginRequest;
import com.aitho.contocorrente.dto.request.SignupRequest;
import com.aitho.contocorrente.dto.request.TokenRefreshRequest;
import com.aitho.contocorrente.dto.response.JwtResponse;
import com.aitho.contocorrente.dto.response.MessageResponse;
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
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest httpServletRequest) {
        JwtResponse response = service.signin(loginRequest, httpServletRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return service.registerCustomer(signUpRequest);
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest tokenRefreshRequest, HttpServletRequest httpServletRequest) throws BadJOSEException, ParseException, JOSEException {
        return service.refreshToken(tokenRefreshRequest, httpServletRequest.getRequestURL().toString());
    }
}
