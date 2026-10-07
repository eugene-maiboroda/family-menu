package com.familymenu.backend.product.service.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String id) {
        super("Product not found: " + id);
    }
}
