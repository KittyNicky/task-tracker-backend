package com.kittynicky.tasktrackerbackend.dto;

import com.kittynicky.tasktrackerbackend.database.entity.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
    private UUID id;
    private String title;
    private String text;
    private TaskStatus status;
}
