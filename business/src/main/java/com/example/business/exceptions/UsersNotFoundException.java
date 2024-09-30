package com.example.business.exceptions;

import java.util.List;

public class UsersNotFoundException extends RuntimeException {
    public UsersNotFoundException(List<Long> notFoundedIds) {
        super("Błąd: Użytkownicy o tych 'id' nie istnieją: " + notFoundedIds);
    }
}
