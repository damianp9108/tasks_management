package com.example.business.exceptions;

public class IllegalStatusException extends RuntimeException {
    public IllegalStatusException(String status) {
        super("Błąd: Nieprawidłowy status '" + status + "'. Możliwe statusy do wyboru to: 'TODO', 'IN_PROGRESS', 'DONE'");
    }
}
