package com.kittynicky.tasktrackerbackend.controller;

import com.kittynicky.tasktrackerbackend.dto.UserDto;
import com.kittynicky.tasktrackerbackend.mapper.UserDtoMapper;
import com.kittynicky.tasktrackerbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping(value = "/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserDtoMapper userDtoMapper;

    @GetMapping
    @SneakyThrows
    public ResponseEntity<UserDto> getUser(Principal principal) {
        var userDto = userDtoMapper.map(
                userService.findByUsername(principal.getName())
        );

        return ResponseEntity.ok(userDto);
    }
}
