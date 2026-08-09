package com.familymenu.backend.domain.dish;

import lombok.Builder;

@Builder(toBuilder = true)
public record Ingredient(

        String id,

        String name
) {

    public Ingredient {
        if(name == null || name.isBlank()) {
            throw new IllegalArgumentException("Ingredient name must not be null or blank");
        }
    }
}
