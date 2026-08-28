package com.familymenu.backend.shopping.domain;

import java.math.BigDecimal;

public record ShoppingIngredient(
        String ingredientId,
        String ingredientName,
        BigDecimal amount,
        Unit unit
) {
}
