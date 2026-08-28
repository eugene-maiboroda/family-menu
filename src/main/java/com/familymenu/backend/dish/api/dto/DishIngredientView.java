package com.familymenu.backend.dish.api.dto;

import java.math.BigDecimal;

public record DishIngredientView(
        String ingredientId,
        String ingredientName,
        BigDecimal amount,
        String unit
) {
}
