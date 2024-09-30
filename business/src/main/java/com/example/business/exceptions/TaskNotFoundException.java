package com.example.business.exceptions;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(Long id) {
        super("Nie znaleziono zadania o podanym id: " + id);
    }
}
