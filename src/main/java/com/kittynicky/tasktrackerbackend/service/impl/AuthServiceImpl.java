package com.kittynicky.tasktrackerbackend.service.impl;

import com.kittynicky.tasktrackerbackend.database.entity.Token;
import com.kittynicky.tasktrackerbackend.database.entity.User;
import com.kittynicky.tasktrackerbackend.database.repository.TokenRepository;
import com.kittynicky.tasktrackerbackend.dto.JwtAuthenticationResponse;
import com.kittynicky.tasktrackerbackend.dto.SignInRequest;
import com.kittynicky.tasktrackerbackend.dto.SignUpRequest;
import com.kittynicky.tasktrackerbackend.kafka.KafkaProducer;
import com.kittynicky.tasktrackerbackend.mapper.GreetingMailResponseMapper;
import com.kittynicky.tasktrackerbackend.mapper.SignUpRequestMapper;
import com.kittynicky.tasktrackerbackend.service.AuthService;
import com.kittynicky.tasktrackerbackend.service.JwtService;
import com.kittynicky.tasktrackerbackend.service.UserService;
import com.kittynicky.tasktrackerbackend.utils.Variables;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final KafkaProducer kafkaProducer;
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final SignUpRequestMapper signUpRequestMapper;
    private final GreetingMailResponseMapper greetingMailResponseMapper;
    private final TokenRepository tokenRepository;

    @Value("${spring.kafka.email-sending-tasks-topic}")
    private String topic;

    @Override
    public ResponseEntity<JwtAuthenticationResponse> signUp(SignUpRequest request) {
        log.info("Sign up request: {}", request);

        var user = signUpRequestMapper.map(request);

        userService.create(user);

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        var jwtAuthenticationResponse = createTokens(user);

        kafkaProducer.sendMessage(topic, greetingMailResponseMapper.map(user));

        return new ResponseEntity<>(jwtAuthenticationResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<JwtAuthenticationResponse> signIn(SignInRequest request) {
        log.info("Sign in request: {}", request);

        var user = userService.findByUsername(request.getUsername());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        var jwtAuthenticationResponse = createTokens(user);

        return new ResponseEntity<>(jwtAuthenticationResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<JwtAuthenticationResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) {

        String authHeader = request.getHeader(Variables.AUTH_HEADER_KEY);

        if (authHeader == null || !authHeader.startsWith(Variables.AUTH_HEADER_VALUE)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.substring(Variables.AUTH_HEADER_VALUE.length());
        String username = jwtService.extractUsername(token);

        User user = userService.findByUsername(username);

        if (jwtService.isValidRefreshToken(token, user)) {
            var jwtAuthenticationResponse = createTokens(user);

            return new ResponseEntity<>(jwtAuthenticationResponse, HttpStatus.OK);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }


    private void revokeAllTokens(User user) {
        var tokens = tokenRepository.findAllByUser(user.getId());

        if (!tokens.isEmpty()) {
            tokens.forEach(token -> token.setLoggedOut(true));
        }
        tokenRepository.saveAll(tokens);
    }

    private void saveUserTokens(String accessToken, String refreshToken, User user) {
        var token = Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .isLoggedOut(false)
                .user(user)
                .build();

        tokenRepository.save(token);
    }

    private JwtAuthenticationResponse createTokens(User user) {
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        revokeAllTokens(user);

        saveUserTokens(accessToken, refreshToken, user);

        return new JwtAuthenticationResponse(accessToken, refreshToken);
    }
}
