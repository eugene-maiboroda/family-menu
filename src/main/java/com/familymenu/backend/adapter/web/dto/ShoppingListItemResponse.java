package com.familymenu.backend.adapter.web.dto;

import com.familymenu.backend.domain.dish.Unit;

import java.math.BigDecimal;

public record ShoppingListItemResponse(

        String ingredientId,

        String ingredientName,

        BigDecimal amount,

        Unit unit
) {
}