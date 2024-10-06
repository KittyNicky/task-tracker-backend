package com.kittynicky.tasktrackerbackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignInRequest {
    @NotBlank(message = "Username must not be empty")
    @Size(min = 3, max = 64, message = "Username must contain from 3 to 64 characters")
    private String username;

    @NotBlank(message = "Password must not be empty")
    @Size(min = 3, max = 255, message = "Password must contain from 3 to 255 characters")
    private String password;
}
