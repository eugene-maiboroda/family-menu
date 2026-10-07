package com.familymenu.backend.infrastructure.web;

import com.familymenu.backend.dish.service.DishNotFoundException;
import com.familymenu.backend.dish.service.DishProductNotFoundException;
import com.familymenu.backend.menu.service.exception.MenuNotFoundException;
import com.familymenu.backend.product.service.exception.ProductAlreadyExistsException;
import com.familymenu.backend.product.service.exception.ProductNotFoundException;
import com.familymenu.backend.shopping.service.exception.InboxModificationException;
import com.familymenu.backend.shopping.service.exception.ShoppingDishNotFoundException;
import com.familymenu.backend.shopping.service.exception.ShoppingListNotFoundException;
import com.familymenu.backend.shopping.service.exception.ShoppingMenuNotFoundException;
import com.familymenu.backend.shopping.service.exception.ShoppingProductNotFoundException;
import com.familymenu.backend.shopping.service.exception.ShoppingSourceInUseException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler({
            DishNotFoundException.class,
            DishProductNotFoundException.class,
            MenuNotFoundException.class,
            ProductNotFoundException.class,
            ShoppingDishNotFoundException.class,
            ShoppingListNotFoundException.class,
            ShoppingMenuNotFoundException.class,
            ShoppingProductNotFoundException.class
    })
    public ProblemDetail handleNotFound(RuntimeException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleInvalidRequest(IllegalArgumentException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationFailure(MethodArgumentNotValidException exception) {
        List<String> validationErrors = exception.getBindingResult().getAllErrors().stream()
                .map(error -> error.getDefaultMessage())
                .toList();
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Invalid request");
        problem.setProperty("errors", validationErrors);
        return problem;
    }

    @ExceptionHandler({
            InboxModificationException.class,
            ShoppingSourceInUseException.class,
            ProductAlreadyExistsException.class
    })
    public ProblemDetail handleConflict(RuntimeException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ProblemDetail handleConcurrentUpdate(OptimisticLockingFailureException exception) {
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT, "The record was changed by another request; reload it and try again");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataConflict(DataIntegrityViolationException exception) {
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT, "Duplicate record or record still referenced by a dish or shopping list");
    }
}
