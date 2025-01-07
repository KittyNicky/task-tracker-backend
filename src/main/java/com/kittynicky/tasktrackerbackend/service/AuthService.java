package com.kittynicky.tasktrackerbackend.service;

import com.kittynicky.tasktrackerbackend.dto.JwtAuthenticationResponse;
import com.kittynicky.tasktrackerbackend.dto.SignInRequest;
import com.kittynicky.tasktrackerbackend.dto.SignUpRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<JwtAuthenticationResponse> signIn(SignInRequest signInRequest);

    ResponseEntity<JwtAuthenticationResponse> signUp(SignUpRequest signUpRequest);

    ResponseEntity<JwtAuthenticationResponse> refreshToken(HttpServletRequest request, HttpServletResponse response);
}
