package com.kittynicky.tasktrackerbackend.dto;

import com.kittynicky.tasktrackerbackend.database.entity.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {
    private String title;
    private String text;
    private TaskStatus status;
}
