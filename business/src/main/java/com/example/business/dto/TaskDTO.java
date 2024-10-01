package com.example.business.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class TaskDTO {

    private String title;
    private String description;
    private String status;
    private LocalDate dueDate;
    private List<Long> userIds;
}
