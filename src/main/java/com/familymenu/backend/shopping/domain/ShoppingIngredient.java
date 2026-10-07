package com.familymenu.backend.shopping.domain;

import java.math.BigDecimal;

public record ShoppingIngredient(

        String productId,

        String productName,

        BigDecimal amount,

        Unit unit
) {
}
