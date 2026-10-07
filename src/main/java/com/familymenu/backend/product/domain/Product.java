package com.familymenu.backend.product.domain;

public record Product(

        String id,

        String name
) {

    public Product {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name must not be null or blank");
        }
    }
}
