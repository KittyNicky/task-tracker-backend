package com.kittynicky.tasktrackerbackend.controller;

import com.kittynicky.tasktrackerbackend.mapper.UserResponseMapper;
import com.kittynicky.tasktrackerbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;
import java.security.Principal;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserResponseMapper userResponseMapper;

    @GetMapping
    @SneakyThrows
    public ResponseEntity<?> getUser(Principal principal) {
        if (principal == null) {
            throw new AuthenticationException("Principal is null");
        }
        var user = userService.findByUsername(principal.getName());

        return ResponseEntity.ok(userResponseMapper.map(user));
    }
}
