package com.example.business.mappers;

import com.example.business.dto.TaskDTO;
import com.example.business.exceptions.IllegalStatusException;
import com.example.domain.entity.Task;
import com.example.domain.entity.TaskStatus;
import com.example.domain.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskMapper {


    public TaskDTO mapToTaskDTO(Task task) {
        return TaskDTO.builder()
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus().name())
                .dueDate(task.getDueDate())
                .userIds(task.getAssignedUsers()
                        .stream()
                        .map(User::getId)
                        .toList())
                .build();
    }

    public List<TaskDTO> mapToTaskDTOs(List<Task> tasksByFilters) {
        return tasksByFilters.stream()
                .map(this::mapToTaskDTO)
                .toList();
    }

    public Task mapToTask(TaskDTO taskDTO, List<User> assignedUsers) {
        return Task.builder()
                .title(taskDTO.getTitle())
                .description(taskDTO.getDescription())
                .status(getTaskStatus(taskDTO.getStatus()))
                .dueDate(taskDTO.getDueDate())
                .assignedUsers(assignedUsers)
                .build();
    }

    public TaskStatus getTaskStatus(String status) {
        for (TaskStatus taskStatus : TaskStatus.values()) {
            if (taskStatus.name().equalsIgnoreCase(status)) {
                return taskStatus;
            }
        }
        throw new IllegalStatusException(status);
    }
}
