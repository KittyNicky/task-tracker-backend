package com.kittynicky.tasktrackerbackend.service;

import com.kittynicky.tasktrackerbackend.database.entity.User;
import com.kittynicky.tasktrackerbackend.database.entity.Task;
import com.kittynicky.tasktrackerbackend.database.repository.TaskRepository;
import com.kittynicky.tasktrackerbackend.dto.TaskRequest;
import com.kittynicky.tasktrackerbackend.dto.TaskResponse;
import com.kittynicky.tasktrackerbackend.mapper.TaskRequestMapper;
import com.kittynicky.tasktrackerbackend.mapper.TaskResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskRequestMapper taskRequestMapper;
    private final TaskResponseMapper taskResponseMapper;
    private final UserService userService;

    public List<Task> findAllByUser(User user) {
        return taskRepository.findAllByUser(user);
    }

    public TaskResponse findById(Principal principal,
                                 UUID id) {
        var user = userService.getUser(principal);

        return taskRepository.findById(id)
                .filter(entity -> entity.getUser().equals(user))
                .map(taskResponseMapper::map)
                .orElseThrow();
    }

    @Transactional
    public TaskResponse update(Principal principal,
                               UUID id,
                               TaskRequest taskRequest) {
        var user = userService.getUser(principal);

        return taskRepository.findById(id)
                .map(entity -> {
                    taskRequestMapper.map(taskRequest, entity);
                    entity.setUser(user);
                    entity.setUpdatedAt(LocalDateTime.now());
                    return entity;
                })
                .map(taskRepository::saveAndFlush)
                .map(taskResponseMapper::map)
                .orElseThrow();
    }

    @Transactional
    public boolean delete(Principal principal,
                          UUID id) {
        return taskRepository.findById(id)
                .filter(entity -> entity.getUser().equals(userService.getUser(principal)))
                .map(entity -> {
                    taskRepository.delete(entity);
                    taskRepository.flush();
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    public TaskResponse create(Principal principal,
                               TaskRequest taskRequest) {
        var user = userService.getUser(principal);

        return Optional.of(taskRequest)
                .map(taskRequestMapper::map)
                .map(entity -> {
                    entity.setUser(user);
                    return entity;
                })
                .map(taskRepository::save)
                .map(taskResponseMapper::map)
                .orElseThrow();
    }
}