package com.familymenu.backend.shopping.service;

public class ShoppingDishNotFoundException extends RuntimeException {

    public ShoppingDishNotFoundException(String message) {
        super(message);
    }
}
