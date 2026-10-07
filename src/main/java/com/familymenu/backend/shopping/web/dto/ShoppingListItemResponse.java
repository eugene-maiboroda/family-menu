package com.familymenu.backend.shopping.web.dto;

import java.math.BigDecimal;

public record ShoppingListItemResponse(

        String productId,

        String productName,

        BigDecimal amount,

        String unit
) {
}
