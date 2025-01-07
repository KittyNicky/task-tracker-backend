package com.kittynicky.tasktrackerbackend.controller;

import com.kittynicky.tasktrackerbackend.dto.JwtAuthenticationResponse;
import com.kittynicky.tasktrackerbackend.dto.SignInRequest;
import com.kittynicky.tasktrackerbackend.dto.SignUpRequest;
import com.kittynicky.tasktrackerbackend.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<JwtAuthenticationResponse> signUp(
            @RequestBody @Valid SignUpRequest request) {

        return authService.signUp(request);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<JwtAuthenticationResponse> signIn(
            @RequestBody @Valid SignInRequest request) {

        return authService.signIn(request);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<JwtAuthenticationResponse> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) {

        return authService.refreshToken(request, response);
    }
}