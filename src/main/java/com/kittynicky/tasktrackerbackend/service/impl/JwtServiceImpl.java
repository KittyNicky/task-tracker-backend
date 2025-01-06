package com.kittynicky.tasktrackerbackend.service.impl;

import com.kittynicky.tasktrackerbackend.database.repository.TokenRepository;
import com.kittynicky.tasktrackerbackend.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private final TokenRepository tokenRepository;
    @Value("${security.jwt.secret_key}")
    private String secretKey;
    @Value("${security.jwt.access_token_expiration}")
    private long accessTokenExpiration;
    @Value("${security.jwt.refresh_token_expiration}")
    private long refreshTokenExpiration;

    @Override
    public String generateAccessToken(UserDetails user) {
        return generateToken(user, accessTokenExpiration);
    }

    @Override
    public String generateRefreshToken(UserDetails user) {
        return generateToken(user, refreshTokenExpiration);
    }

    @Override
    public boolean isValidAccessToken(String token, UserDetails user) {
        String username = extractUsername(token);

        boolean isValidAccessToken = tokenRepository.findByAccessToken(token)
                .map(t -> !t.isLoggedOut())
                .orElse(false);

        return username.equals(user.getUsername())
               && isAccessTokenExpired(token)
               && isValidAccessToken;
    }

    @Override
    public boolean isValidRefreshToken(String token, UserDetails user) {
        String username = extractUsername(token);

        boolean isValidRefreshToken = tokenRepository.findByRefreshToken(token)
                .map(t -> !t.isLoggedOut()).orElse(false);

        return username.equals(user.getUsername())
               && isAccessTokenExpired(token)
               && isValidRefreshToken;
    }

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private  <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    private String generateToken(UserDetails user, long expireTime) {
        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(getSigningKey())
                .compact();
    }

    private boolean isAccessTokenExpired(String token) {
        return !extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        JwtParserBuilder parser = Jwts.parser();
        parser.verifyWith(getSigningKey());

        return parser.build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}