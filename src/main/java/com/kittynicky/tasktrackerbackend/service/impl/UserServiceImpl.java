package com.kittynicky.tasktrackerbackend.service.impl;

import com.kittynicky.tasktrackerbackend.database.entity.User;
import com.kittynicky.tasktrackerbackend.database.repository.UserRepository;
import com.kittynicky.tasktrackerbackend.dto.UserDto;
import com.kittynicky.tasktrackerbackend.mapper.UserDtoMapper;
import com.kittynicky.tasktrackerbackend.service.UserService;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import java.security.Principal;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;

    @Override
    @Transactional
    public UserDto create(User user) {
        log.info("Creating new user: {}", user);

        if (existsByUsername(user.getUsername())) {
            log.error("User with username='{}' already exists", user.getUsername());

            throw new EntityExistsException("User with username='" + user.getUsername() + "' already exists");
        }
        if (existsByEmail(user.getEmail())) {
            log.error("User with email='{}' already exists", user.getEmail());

            throw new EntityExistsException("User with email='" + user.getEmail() + "' already exists");
        }

        return userDtoMapper.map(userRepository.save(user));
    }

    @Override
    public User findByUsername(String username) {
        log.info("Finding user with username='{}'", username);

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username='" + username + "' not found"));
    }

    @Override
    @SneakyThrows
    public User getUser(Principal principal) {
        if (principal == null) {
            log.error("Principal is null");
            throw new AuthenticationException("User is unauthorized, his session expired or terminated via logout");
        }

        return findByUsername(principal.getName());
    }

    @Override
    public boolean existsByUsername(String username) {
        log.info("Checking if user with username='{}' exists", username);

        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    public boolean existsByEmail(String email) {
        log.info("Checking if user with email='{}' exists", email);

        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Loading if user exists by username='{}'", username);

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username='" + username + "' not found"));
    }
}