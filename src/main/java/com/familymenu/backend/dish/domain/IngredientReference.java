package com.familymenu.backend.dish.domain;

public record IngredientReference(
        String id,
        String name
) {

    public IngredientReference {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Ingredient name must not be null or blank");
        }
    }
}
