package com.kittynicky.tasktrackerbackend.dto;

import com.kittynicky.tasktrackerbackend.database.entity.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskRequest {
    @NotBlank(message = "Title must not be empty")
    private String title;
    private String text;
    private TaskStatus status;
}
