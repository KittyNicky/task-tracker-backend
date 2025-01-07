package com.kittynicky.tasktrackerbackend.controller;

import com.kittynicky.tasktrackerbackend.database.entity.Task;
import com.kittynicky.tasktrackerbackend.dto.TaskRequest;
import com.kittynicky.tasktrackerbackend.dto.TaskResponse;
import com.kittynicky.tasktrackerbackend.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.*;

@Slf4j
@RestController
@RequestMapping(value = "/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<Task>> getAll(Principal principal) {

        return ok(taskService.findAll(principal));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getById(
            Principal principal,
            @PathVariable UUID id) {

        return ok(taskService.findById(principal, id));
    }

    @PostMapping
    public ResponseEntity<TaskResponse> create(
            Principal principal,
            @RequestBody @Valid TaskRequest taskRequest) {

        return ok(taskService.create(principal, taskRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> update(
            Principal principal,
            @PathVariable UUID id,
            @RequestBody @Valid TaskRequest taskRequest) {

        return ok(taskService.update(principal, id, taskRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TaskResponse> delete(
            Principal principal,
            @PathVariable UUID id) {

        return taskService.delete(principal, id)
                ? noContent().build()
                : notFound().build();
    }
}
