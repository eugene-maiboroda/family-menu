package com.familymenu.backend.dish.domain;

public record ProductReference(

        String id,

        String name
) {

    public ProductReference {
        if ((id == null || id.isBlank()) && (name == null || name.isBlank())) {
            throw new IllegalArgumentException("Product ID or name is required");
        }
    }
}
