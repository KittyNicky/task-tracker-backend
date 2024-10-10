package com.kittynicky.tasktrackerbackend.controller;

import com.kittynicky.tasktrackerbackend.dto.TaskRequest;
import com.kittynicky.tasktrackerbackend.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.*;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<?> getAll(Principal principal) {
        log.info("Get all tasks");

        return ok(taskService.findAll(principal));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(Principal principal,
                                     @PathVariable UUID id) {
        log.info("Get task by id: {}", id);

        return ok(taskService.findById(principal, id));
    }

    @PostMapping
    public ResponseEntity<?> create(Principal principal,
                                    @RequestBody @Valid TaskRequest taskRequest) {
        log.info("Create task: {}", taskRequest);

        return ok(taskService.create(principal, taskRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(Principal principal,
                                    @PathVariable UUID id,
                                    @RequestBody @Valid TaskRequest taskRequest) {
        log.info("Update task by id: {}, TaskRequest: {}", id, taskRequest);

        return ok(taskService.update(principal, id, taskRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(Principal principal,
                                    @PathVariable UUID id) {
        log.info("Delete task: {}", id);

        return taskService.delete(principal, id)
                ? noContent().build()
                : notFound().build();
    }
}
