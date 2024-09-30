package com.example.business.exceptions;

public class FilteredUsersNotFoundException extends RuntimeException {
    public FilteredUsersNotFoundException() {
        super("Nie znaleziono żadnych użytkowników spełniających kryteria wyszukiwania.");
    }
}
