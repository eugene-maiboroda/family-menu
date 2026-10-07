package com.familymenu.backend.shopping.service.exception;

public class ShoppingListNotFoundException extends RuntimeException {
    public ShoppingListNotFoundException(String id) {
        super("Shopping list or item not found: " + id);
    }
}
