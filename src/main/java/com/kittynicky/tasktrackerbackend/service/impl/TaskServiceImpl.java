package com.kittynicky.tasktrackerbackend.service.impl;

import com.kittynicky.tasktrackerbackend.database.entity.Task;
import com.kittynicky.tasktrackerbackend.database.repository.TaskRepository;
import com.kittynicky.tasktrackerbackend.dto.TaskRequest;
import com.kittynicky.tasktrackerbackend.dto.TaskResponse;
import com.kittynicky.tasktrackerbackend.mapper.TaskMapper;
import com.kittynicky.tasktrackerbackend.mapper.TaskResponseMapper;
import com.kittynicky.tasktrackerbackend.service.TaskService;
import com.kittynicky.tasktrackerbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final TaskResponseMapper taskResponseMapper;
    private final UserService userService;

    @Override
    public List<Task> findAll(Principal principal) {
        log.info("Finding all tasks");

        var user = userService.getUser(principal);

        return taskRepository.findAllByUser(user);
    }

    public TaskResponse findById(Principal principal,
                                 UUID id) {
        log.info("Finding task with id {}", id);

        var user = userService.getUser(principal);

        return taskRepository.findById(id)
                .filter(entity -> entity.getUser().equals(user))
                .map(taskResponseMapper::map)
                .orElseThrow();
    }

    @Override
    @Transactional
    public TaskResponse create(Principal principal,
                               TaskRequest taskRequest) {
        log.info("Creating task={}", taskRequest);

        var user = userService.getUser(principal);

        return Optional.of(taskRequest)
                .map(taskMapper::map)
                .map(entity -> {
                    entity.setUser(user);
                    entity.setUpdatedAt(LocalDateTime.now());
                    return entity;
                })
                .map(taskRepository::save)
                .map(taskResponseMapper::map)
                .orElseThrow();
    }

    @Override
    @Transactional
    public TaskResponse update(Principal principal,
                               UUID id,
                               TaskRequest taskRequest) {
        log.info("Updating task with id {}", id);

        var user = userService.getUser(principal);

        return taskRepository.findById(id)
                .map(entity -> {
                    taskMapper.map(taskRequest, entity);
                    entity.setUser(user);
                    entity.setUpdatedAt(LocalDateTime.now());
                    return entity;
                })
                .map(taskRepository::saveAndFlush)
                .map(taskResponseMapper::map)
                .orElseThrow();
    }

    @Override
    @Transactional
    public boolean delete(Principal principal,
                          UUID id) {
        log.info("Deleting task with id {}", id);

        var user = userService.getUser(principal);

        return taskRepository.findById(id)
                .filter(entity -> entity.getUser().equals(user))
                .map(entity -> {
                    taskRepository.delete(entity);
                    taskRepository.flush();
                    return true;
                })
                .orElse(false);
    }
}
