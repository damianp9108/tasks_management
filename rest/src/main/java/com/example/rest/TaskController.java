package com.example.rest;

import com.example.business.dto.TaskDTO;
import com.example.business.service.TaskService;
import com.example.domain.entity.Task;
import com.example.domain.entity.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public List<Task> getAllTasks(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) LocalDate dueDate) {
        return taskService.getTasksWithFilters(status, title, dueDate);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody TaskDTO taskDTO) {
        try {
            Task createdTask = taskService.createTask(taskDTO);
            return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTask(@PathVariable Long id, @RequestBody TaskDTO taskDTO) {
        try {
            Task updatedTask = taskService.updateTask(id, taskDTO);
            return ResponseEntity.ok(updatedTask);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("ERROR: " + e.getMessage());
        }
    }

    // Endpoint do zmiany statusu zadania
    @PutMapping("/{id}/{status}")
    public ResponseEntity<Task> updateTaskStatus(@PathVariable Long id, @PathVariable String status) {
        try {
            Task updatedTask = taskService.updateTaskStatus(id, status);
            return ResponseEntity.ok(updatedTask);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}/{userId}")
    public ResponseEntity<Task> addUserToTask(@PathVariable Long id, @PathVariable Long userId) {
        try {
            Task updatedTask = taskService.addUserToTask(id, userId);
            return ResponseEntity.ok(updatedTask);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
