package com.familymenu.backend.dish.domain;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.Objects;

@Builder(toBuilder = true)
public record DishIngredient(

        String id,

        ProductReference product,

        BigDecimal amount,

        Unit unit
) {

    public DishIngredient {

        Objects.requireNonNull(product, "Ingredient must not be null");
        Objects.requireNonNull(amount, "Amount must not be null");
        Objects.requireNonNull(unit, "Unit must not be null");

        if (amount.signum() <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }
}
