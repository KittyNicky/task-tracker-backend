package com.kittynicky.tasktrackerbackend.mapper;

import com.kittynicky.tasktrackerbackend.database.entity.Role;
import com.kittynicky.tasktrackerbackend.database.entity.User;
import com.kittynicky.tasktrackerbackend.dto.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SignUpRequestMapper implements Mapper<SignUpRequest, User> {
    private final PasswordEncoder passwordEncoder;

    @Override
    public User map(SignUpRequest from) {
        return User.builder()
                .username(from.getUsername())
                .email(from.getEmail())
                .role(Role.USER)
                .password(passwordEncoder.encode(from.getPassword()))
                .build();
    }
}
