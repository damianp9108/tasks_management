package com.example.business.service;

import com.example.business.dto.TaskDTO;
import com.example.business.exceptions.*;
import com.example.business.mappers.TaskMapper;
import com.example.domain.entity.Task;
import com.example.domain.entity.TaskStatus;
import com.example.domain.entity.User;
import com.example.domain.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final UserService userService;
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public List<TaskDTO> getFilteredTasks(TaskStatus status, String title, LocalDate dueDate) {
        List<Task> tasksByFilters = taskRepository.findByFilters(status, title, dueDate);
        if (tasksByFilters.isEmpty()) {
            throw new FilteredTasksNotFoundException();
        }
        return taskMapper.mapToTaskDTOs(tasksByFilters);
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    }

    public TaskDTO createTask(TaskDTO taskDTO) {
        List<User> assignedUsers = getUsersToAssign(taskDTO);
        Task newTask = taskMapper.mapToTask(taskDTO, assignedUsers);
        taskRepository.save(newTask);
        return taskDTO;
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

    public TaskDTO updateTask(Long id, TaskDTO taskDTO) {
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
            existingTask.setStatus(taskMapper.getTaskStatus(taskDTO.getStatus()));
        }
        if (taskDTO.getDueDate() != null) {
            existingTask.setDueDate(taskDTO.getDueDate());
        }
        if (!usersToAssign.isEmpty()) {
            usersToAssign.stream()
                    .filter(user -> !assignedUsers.contains(user))
                    .forEach(assignedUsers::add);
        }

        taskRepository.save(existingTask);
        return taskMapper.mapToTaskDTO(existingTask);

    }

    public TaskDTO updateTaskStatus(Long id, String status) {
        Task taskById = getTaskById(id);
        taskById.setStatus(taskMapper.getTaskStatus(status));
        taskRepository.save(taskById);
        return taskMapper.mapToTaskDTO(taskById);
    }

    public TaskDTO addUserToTask(Long taskId, Long userId) {
        Task taskById = getTaskById(taskId);
        User userById = userService.findUserById(userId);

        if (!taskById.getAssignedUsers().contains(userById)) {
            taskById.getAssignedUsers().add(userById);
        }
        taskRepository.save(taskById);
        return taskMapper.mapToTaskDTO(taskById);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}

