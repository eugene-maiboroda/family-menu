package com.familymenu.backend.shopping.domain;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ShoppingListItem(

        String ingredientId,

        String ingredientName,

        BigDecimal amount,

        Unit unit
) {
}
