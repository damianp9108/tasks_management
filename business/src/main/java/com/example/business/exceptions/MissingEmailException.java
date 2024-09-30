package com.example.business.exceptions;

public class MissingEmailException extends RuntimeException {
    public MissingEmailException() {
        super("Błąd: Adres email jest wymagany i nie może być pusty");
    }
}
