package com.kittynicky.tasktrackerbackend.mapper;

import com.kittynicky.tasktrackerbackend.database.entity.Task;
import com.kittynicky.tasktrackerbackend.dto.TaskRequest;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper implements Mapper<TaskRequest, Task> {
    @Override
    public Task map(TaskRequest from) {
        Task to = new Task();
        copy(from, to);
        return to;
    }

    @Override
    public Task map(TaskRequest from, Task to) {
        copy(from, to);
        return to;
    }

    private void copy(TaskRequest from, Task to) {
        to.setTitle(from.getTitle());
        to.setText(from.getText());
        to.setStatus(from.getStatus());
    }
}
