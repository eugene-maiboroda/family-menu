package com.familymenu.backend.dish.api.dto;

import java.math.BigDecimal;

public record DishIngredientView(
        String productId,
        String productName,
        BigDecimal amount,
        String unit
) {
}
