package com.familymenu.backend.dish.web.dto;

import java.math.BigDecimal;

public record DishIngredientResponse(
        String ingredientId,
        String ingredientName,
        BigDecimal amount,
        String unit
) {
}
