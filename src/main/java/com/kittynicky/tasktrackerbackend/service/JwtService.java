package com.kittynicky.tasktrackerbackend.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String generateAccessToken(UserDetails user);

    String generateRefreshToken(UserDetails user);

    boolean isValidAccessToken(String token, UserDetails user);

    boolean isValidRefreshToken(String token, UserDetails user);

    String extractUsername(String token);
}
