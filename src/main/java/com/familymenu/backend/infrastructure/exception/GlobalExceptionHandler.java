package com.familymenu.backend.infrastructure.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DishNotFoundException.class)
    public String handleDishNotFoundException(DishNotFoundException ex) {
        return ex.getMessage();
    }
}
