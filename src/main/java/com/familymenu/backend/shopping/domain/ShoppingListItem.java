package com.familymenu.backend.shopping.domain;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ShoppingListItem(

        String productId,

        String productName,

        BigDecimal amount,

        Unit unit,

        boolean purchased
) {
    public ShoppingListItem(String productId, String productName, BigDecimal amount, Unit unit) {
        this(productId, productName, amount, unit, false);
    }
}
