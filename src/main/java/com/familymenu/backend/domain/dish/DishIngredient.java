package com.familymenu.backend.domain.dish;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record DishIngredient(

        Ingredient ingredient,

        BigDecimal amount,

        Unit unit
) {
}
