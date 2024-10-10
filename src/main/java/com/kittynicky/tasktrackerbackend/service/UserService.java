package com.kittynicky.tasktrackerbackend.service;

import com.kittynicky.tasktrackerbackend.database.entity.User;
import com.kittynicky.tasktrackerbackend.database.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import java.security.Principal;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public User create(User user) {
        log.info("Creating new user: {}", user);

        if (userRepository.existsByUsername(user.getUsername())) {
            log.error("User with username='{}' already exists", user.getUsername());
            throw new EntityExistsException("User with username='" + user.getUsername() + "' already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            log.error("User with email='{}' already exists", user.getEmail());
            throw new EntityExistsException("User with email='" + user.getEmail() + "' already exists");
        }

        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        log.info("Finding user with username='{}'", username);

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User with username='" + username + "' not found"));
    }

    public UserDetailsService userDetailsService() {
        log.info("Finding user details service");

        return this::findByUsername;
    }

    @SneakyThrows
    public User getUser(Principal principal) {
        if (principal == null) {
            log.error("Principal is null");
            throw new AuthenticationException("User is unauthorized, his session expired or terminated via logout");
        }
        return findByUsername(principal.getName());
    }
}