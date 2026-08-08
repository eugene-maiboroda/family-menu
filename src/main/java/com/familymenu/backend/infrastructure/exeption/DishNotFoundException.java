package com.familymenu.backend.infrastructure.exeption;

public class DishNotFoundException extends RuntimeException {
    public DishNotFoundException(String message) {
        super(message);
    }
}
