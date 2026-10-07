package com.familymenu.backend.shopping.service.exception;

public class ShoppingProductNotFoundException extends RuntimeException {

    public ShoppingProductNotFoundException(String productId) {
        super("Product not found: " + productId);
    }
}
