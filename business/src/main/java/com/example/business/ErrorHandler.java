package com.example.business;

import com.example.business.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(UsersNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String usersNotFoundErrorHandler(UsersNotFoundException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String userNotFoundErrorHandler(UserNotFoundException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public String emailAlreadyExistsErrorHandler(EmailAlreadyExistsException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(IllegalStatusException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String illegalStatusErrorHandler(IllegalStatusException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(TaskNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String taskNotFoundErrorHandler(TaskNotFoundException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(FilteredUsersNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String filteredUsersNotFoundErrorHandler(FilteredUsersNotFoundException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(FilteredTasksNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String filteredTasksNotFoundErrorHandler(FilteredTasksNotFoundException exception) {
        return exception.getMessage();
    }
}
