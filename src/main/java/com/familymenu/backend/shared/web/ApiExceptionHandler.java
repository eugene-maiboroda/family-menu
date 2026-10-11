package com.familymenu.backend.shared.web;

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
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
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
    public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException exception) {
        return respond(HttpStatus.NOT_FOUND, exception, exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRequest(IllegalArgumentException exception) {
        return respond(HttpStatus.BAD_REQUEST, exception, exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationFailure(MethodArgumentNotValidException exception) {
        Map<String, String> validationErrors = new LinkedHashMap<>();
        for (ObjectError error : exception.getBindingResult().getAllErrors()) {
            String fieldName = error instanceof FieldError fieldError ? fieldError.getField() : "request";
            String message = error.getDefaultMessage();
            validationErrors.putIfAbsent(fieldName, message == null ? "Invalid value" : message);
        }

        ErrorDetailsResponse details = new ErrorDetailsResponse(
                "Invalid request", exception.getClass().getSimpleName(), validationErrors);
        return ResponseEntity.badRequest().body(new ErrorResponse(details));
    }

    @ExceptionHandler({
            InboxModificationException.class,
            ShoppingSourceInUseException.class,
            ProductAlreadyExistsException.class
    })
    public ResponseEntity<ErrorResponse> handleConflict(RuntimeException exception) {
        return respond(HttpStatus.CONFLICT, exception, exception.getMessage());
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<ErrorResponse> handleConcurrentUpdate(OptimisticLockingFailureException exception) {
        return respond(
                HttpStatus.CONFLICT, exception, "The record was changed by another request; reload it and try again");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataConflict(DataIntegrityViolationException exception) {
        return respond(
                HttpStatus.CONFLICT, exception, "Duplicate record or record still referenced by a dish or shopping list");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadableRequest(HttpMessageNotReadableException exception) {
        return respond(HttpStatus.BAD_REQUEST, exception, "Request body is missing or invalid");
    }

    @ExceptionHandler(ErrorResponseException.class)
    public ResponseEntity<ErrorResponse> handleWebFailure(ErrorResponseException exception) {
        ErrorDetailsResponse details = new ErrorDetailsResponse(
                exception.getBody().getDetail(), exception.getClass().getSimpleName());
        return ResponseEntity.status(exception.getStatusCode()).body(new ErrorResponse(details));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedFailure(Exception exception) {
        if (exception instanceof org.springframework.web.ErrorResponse webError) {
            ErrorDetailsResponse details = new ErrorDetailsResponse(
                    webError.getBody().getDetail(), exception.getClass().getSimpleName());
            return ResponseEntity.status(webError.getStatusCode()).body(new ErrorResponse(details));
        }

        log.error("Unexpected request failure", exception);
        return respond(HttpStatus.INTERNAL_SERVER_ERROR, exception, "An unexpected server error occurred");
    }

    private ResponseEntity<ErrorResponse> respond(HttpStatus status, Exception exception, String message) {
        ErrorDetailsResponse details = new ErrorDetailsResponse(message, exception.getClass().getSimpleName());
        return ResponseEntity.status(status).body(new ErrorResponse(details));
    }
}
