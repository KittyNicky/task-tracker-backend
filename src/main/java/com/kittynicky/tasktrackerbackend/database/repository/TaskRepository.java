package com.kittynicky.tasktrackerbackend.database.repository;

import com.kittynicky.tasktrackerbackend.database.entity.User;
import com.kittynicky.tasktrackerbackend.database.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findAllByUser(User user);
}
