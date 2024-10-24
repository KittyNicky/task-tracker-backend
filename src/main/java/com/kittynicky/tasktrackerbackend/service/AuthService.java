package com.kittynicky.tasktrackerbackend.service;

import com.kittynicky.tasktrackerbackend.dto.JwtAuthenticationResponse;
import com.kittynicky.tasktrackerbackend.dto.SignInRequest;
import com.kittynicky.tasktrackerbackend.dto.SignUpRequest;
import com.kittynicky.tasktrackerbackend.kafka.KafkaProducer;
import com.kittynicky.tasktrackerbackend.mapper.GreetingMailResponseMapper;
import com.kittynicky.tasktrackerbackend.mapper.SignUpRequestMapper;
import com.kittynicky.tasktrackerbackend.mapper.UserResponseMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final KafkaProducer kafkaProducer;
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final SignUpRequestMapper signUpRequestMapper;
    private final UserResponseMapper userResponseMapper;
    private final GreetingMailResponseMapper greetingMailResponseMapper;

    @Value("${spring.kafka.email-sending-tasks-topic}")
    private String topic;

    public JwtAuthenticationResponse signUp(SignUpRequest request) {
        log.info("Sign up request: {}", request);

        var user = signUpRequestMapper.map(request);

        userService.create(user);

        var jwt = jwtService.generateToken(user);

        kafkaProducer.sendMessage(topic, greetingMailResponseMapper.map(user));

        return new JwtAuthenticationResponse(jwt);
    }

    public JwtAuthenticationResponse signIn(SignInRequest request) {
        log.info("Sign in request: {}", request);

        if (!isCredentialsValid(request)) {
            log.error("Invalid username or password");
            throw new BadCredentialsException("Invalid username or password");
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        var user = userService
                .userDetailsService()
                .loadUserByUsername(request.getUsername());

        var jwt = jwtService.generateToken(user);

        return new JwtAuthenticationResponse(jwt);
    }

    private boolean isCredentialsValid(SignInRequest request) {
        var user = userService.findByUsername(request.getUsername());
        return passwordEncoder.matches(request.getPassword(), user.getPassword());
    }
}
