package com.kittynicky.tasktrackerbackend.service;

import com.kittynicky.tasktrackerbackend.database.entity.Task;
import com.kittynicky.tasktrackerbackend.dto.TaskRequest;
import com.kittynicky.tasktrackerbackend.dto.TaskResponse;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

public interface TaskService {
    List<Task> findAll(Principal principal);

    TaskResponse findById(Principal principal, UUID id);

    TaskResponse create(Principal principal, TaskRequest taskRequest);

    TaskResponse update(Principal principal, UUID id, TaskRequest taskRequest);

    boolean delete(Principal principal, UUID id);

}
