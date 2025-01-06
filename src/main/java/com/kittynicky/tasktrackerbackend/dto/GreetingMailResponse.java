package com.kittynicky.tasktrackerbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GreetingMailResponse {
    private String subject;
    private String text;
    private String email;
}