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
    public ResponseEntity<List<TaskDTO>> getFilteredTasks(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) LocalDate dueDate) {
        return ResponseEntity.ok(taskService.getFilteredTasks(status, title, dueDate));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));

    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO) {
        return new ResponseEntity<>(taskService.createTask(taskDTO), HttpStatus.CREATED);

    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @RequestBody TaskDTO taskDTO) {
        return ResponseEntity.ok(taskService.updateTask(id, taskDTO));
    }

    @PutMapping("/{id}/{status}")
    public ResponseEntity<TaskDTO> updateTaskStatus(@PathVariable Long id, @PathVariable String status) {
        return ResponseEntity.ok(taskService.updateTaskStatus(id, status));
    }

    @PatchMapping("/{id}/{userId}")
    public ResponseEntity<TaskDTO> addUserToTask(@PathVariable Long id, @PathVariable Long userId) {
        return ResponseEntity.ok(taskService.addUserToTask(id, userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
