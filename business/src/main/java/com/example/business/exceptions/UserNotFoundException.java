package com.example.business.exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(Long id) {
        super("Błąd: Użytkownik o podanym id: " + id + " nie istnieje");
    }
}
