package com.kittynicky.tasktrackerbackend.dto;

import com.kittynicky.tasktrackerbackend.database.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
public class SignUpRequest {
    @NotBlank(message = "Username must not be empty")
    @Size(min = 3, max = 64, message = "Username must contain from 3 to 64 characters")
    private String username;

    @NotBlank(message = "Username must not be empty")
    @Size(min = 5, max = 255, message = "Username must contain from 5 to 255 characters")
    @Email(message = "Email must be in the format user@example.com")
    private String email;

    @NotBlank(message = "Password must not be empty")
    @Size(min = 3, max = 255, message = "Password must contain from 3 to 255 characters")
    @ToString.Exclude
    private String password;

    private Role role;
}
