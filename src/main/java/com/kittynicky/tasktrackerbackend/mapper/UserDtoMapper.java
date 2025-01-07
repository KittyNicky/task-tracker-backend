package com.kittynicky.tasktrackerbackend.mapper;

import com.kittynicky.tasktrackerbackend.database.entity.User;
import com.kittynicky.tasktrackerbackend.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserDtoMapper implements Mapper<User, UserDto> {
    @Override
    public UserDto map(User from) {
        return UserDto.builder()
                .id(from.getId())
                .username(from.getUsername())
                .email(from.getEmail())
                .build();
    }
}
