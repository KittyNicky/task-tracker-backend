package com.kittynicky.tasktrackerbackend.mapper;

import com.kittynicky.tasktrackerbackend.database.entity.Task;
import com.kittynicky.tasktrackerbackend.dto.TaskResponse;
import org.springframework.stereotype.Component;

@Component
public class TaskResponseMapper implements Mapper<Task, TaskResponse> {
    @Override
    public TaskResponse map(Task from) {
        TaskResponse to = new TaskResponse();
        copy(from, to);
        return to;
    }

    @Override
    public TaskResponse map(Task from, TaskResponse to) {
        copy(from, to);
        return to;
    }

    private void copy(Task from, TaskResponse to) {
        to.setId(from.getId());
        to.setTitle(from.getTitle());
        to.setText(from.getText());
        to.setStatus(from.getStatus());
    }

}