package com.example.business.service;

import com.example.business.dto.TaskDTO;
import com.example.business.exceptions.TaskNotFoundException;
import com.example.business.exceptions.UserNotFoundException;
import com.example.business.exceptions.UsersNotFoundException;
import com.example.domain.entity.Task;
import com.example.domain.entity.TaskStatus;
import com.example.domain.entity.User;
import com.example.domain.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final UserService userService;
    private final TaskRepository taskRepository;

    public List<Task> getTasksWithFilters(TaskStatus status, String title, LocalDate dueDate) {
        return taskRepository.findByFilters(status, title, dueDate);
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Task createTask(TaskDTO taskDTO) {

        List<User> assignedUsers = getUsersToAssign(taskDTO);

        Task newTask = new Task();
        newTask.setTitle(taskDTO.getTitle());
        newTask.setDescription(taskDTO.getDescription());
        newTask.setStatus(TaskStatus.valueOf(taskDTO.getStatus()));
        newTask.setDueDate(taskDTO.getDueDate());
        newTask.setAssignedUsers(assignedUsers);

        return taskRepository.save(newTask);
    }

    private List<User> getUsersToAssign(TaskDTO taskDTO) {
        if (taskDTO.getUserIds() != null && !taskDTO.getUserIds().isEmpty()) {
            List<User> assignedUsers = userService.findUsersByIds(taskDTO.getUserIds());

            List<Long> notFoundedUserIds = getNotFoundedUserIds(taskDTO.getUserIds(), assignedUsers);

            if (!notFoundedUserIds.isEmpty()) {
                throw new UsersNotFoundException(notFoundedUserIds);
            }
            return assignedUsers;
        } else return Collections.emptyList();
    }

    private static List<Long> getNotFoundedUserIds(List<Long> userIds, List<User> assignedUsers) {
        Set<Long> foundedUserIds = assignedUsers.stream()
                .map(User::getId)
                .collect(Collectors.toSet());
        return userIds.stream()
                .filter(id -> !foundedUserIds.contains(id))
                .toList();
    }

    public Task updateTask(Long id, TaskDTO taskDTO) {
        Task existingTask = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        List<User> assignedUsers = existingTask.getAssignedUsers();

        List<User> usersToAssign = getUsersToAssign(taskDTO);

        if (taskDTO.getTitle() != null) {
            existingTask.setTitle(taskDTO.getTitle());
        }
        if (taskDTO.getDescription() != null) {
            existingTask.setDescription(taskDTO.getDescription());
        }
        if (taskDTO.getStatus() != null) {
            existingTask.setStatus(TaskStatus.valueOf(taskDTO.getStatus()));
        }
        if (taskDTO.getDueDate() != null) {
            existingTask.setDueDate(taskDTO.getDueDate());
        }
        if (!usersToAssign.isEmpty()) {
            usersToAssign.stream()
                    .filter(user -> !assignedUsers.contains(user))
                    .forEach(assignedUsers::add);
        }

        return taskRepository.save(existingTask);

    }

    public Task updateTaskStatus(Long id, String status) {
        Optional<Task> taskById = taskRepository.findById(id);

        if (taskById.isPresent()) {
            Task task = taskById.get();
            task.setStatus(TaskStatus.valueOf(status));
            return taskRepository.save(task);
        } else {
            throw new IllegalArgumentException("Zadanie o podanym ID nie istnieje");
        }
    }

    public Task addUserToTask(Long taskId, Long userId) {
        Task taskById = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException(taskId));
        User userById = userService.getUserById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        if (!taskById.getAssignedUsers().contains(userById)) {
            taskById.getAssignedUsers().add(userById);
        }

        return taskRepository.save(taskById);

    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}

