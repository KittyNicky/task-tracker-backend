package com.kittynicky.tasktrackerbackend.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String email;
}
