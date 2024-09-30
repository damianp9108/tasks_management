package com.example.business.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class TaskDTO {

    private String title;
    private String description;
    private String status;
    private LocalDate dueDate;
    private List<Long> userIds;
}
