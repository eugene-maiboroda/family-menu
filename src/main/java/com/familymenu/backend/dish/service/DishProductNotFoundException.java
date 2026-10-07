package com.familymenu.backend.dish.service;

public class DishProductNotFoundException extends RuntimeException {
    public DishProductNotFoundException(String productId) {
        super("Product not found: " + productId);
    }
}
