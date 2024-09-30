package com.example.business.exceptions;

public class FilteredTasksNotFoundException extends RuntimeException {
    public FilteredTasksNotFoundException() {
        super("Nie znaleziono zadan spełniających podane kryteria.");
    }
}
