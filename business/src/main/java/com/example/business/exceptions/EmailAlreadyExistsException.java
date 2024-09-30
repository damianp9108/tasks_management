package com.example.business.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) {
        super("Błąd: Istnieje już użytkownik posiadający email: " + email);
    }
}
