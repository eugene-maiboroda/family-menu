package com.familymenu.backend.dish.web;

import com.familymenu.backend.dish.service.DishNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DishExceptionHandler {
    @ExceptionHandler(DishNotFoundException.class)
    public String handleDishNotFoundException(DishNotFoundException ex) {
        return ex.getMessage();
    }
}
