package com.kittynicky.tasktrackerbackend.mapper;

import com.kittynicky.tasktrackerbackend.database.entity.User;
import com.kittynicky.tasktrackerbackend.dto.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserResponseMapper implements Mapper<User, UserResponse> {
    @Override
    public UserResponse map(User from) {
        return UserResponse.builder()
                .id(from.getId())
                .username(from.getUsername())
                .email(from.getEmail())
                .build();
    }
}
