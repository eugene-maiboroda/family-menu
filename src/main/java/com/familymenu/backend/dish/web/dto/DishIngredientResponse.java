package com.familymenu.backend.dish.web.dto;

import java.math.BigDecimal;

public record DishIngredientResponse(
        String productId,
        String productName,
        BigDecimal amount,
        String unit
) {
}
