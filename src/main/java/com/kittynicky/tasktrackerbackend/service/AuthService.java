package com.kittynicky.tasktrackerbackend.service;

import com.kittynicky.tasktrackerbackend.database.entity.Role;
import com.kittynicky.tasktrackerbackend.database.entity.User;
import com.kittynicky.tasktrackerbackend.dto.JwtAuthenticationResponse;
import com.kittynicky.tasktrackerbackend.dto.SignInRequest;
import com.kittynicky.tasktrackerbackend.dto.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationResponse signUp(SignUpRequest request) {
        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userService.create(user);

        var jwt = jwtService.generateToken(user);

        return new JwtAuthenticationResponse(jwt);
    }

    public JwtAuthenticationResponse signIn(SignInRequest request) {
        if (!isCredentialsValid(request)) {
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
        System.out.println();
        return new JwtAuthenticationResponse(jwt);
    }

    private boolean isCredentialsValid(SignInRequest request) {
        var user = userService.findByUsername(request.getUsername());
        return passwordEncoder.matches(request.getPassword(), user.getPassword());
    }
}
