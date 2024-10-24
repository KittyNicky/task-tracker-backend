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
    private final String subject = "Hello, it's a task-tracker-service";
    private String text = """
            <h1>Hello, %s!</h1>
            <br>
            <body>
            <h3>Welcome to the task-tracker-service!</h3>
            You have successfully registered in the service.
            <br> 
            Come on, create your first task!
            </body>
            """;
    private String email;
}

