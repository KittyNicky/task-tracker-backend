package com.kittynicky.tasktrackerbackend.handler;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class ApiResponse {
    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss.ms")
    private String message;
    private LocalDateTime timestamp;

    public ApiResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp  = LocalDateTime.now();
    }
}
