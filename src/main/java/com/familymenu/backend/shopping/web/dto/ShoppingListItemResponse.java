package com.familymenu.backend.shopping.web.dto;

import java.math.BigDecimal;

public record ShoppingListItemResponse(

        String ingredientId,

        String ingredientName,

        BigDecimal amount,

        String unit
) {
}
