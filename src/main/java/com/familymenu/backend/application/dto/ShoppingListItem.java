package com.familymenu.backend.application.dto;

import com.familymenu.backend.domain.Unit;

import java.math.BigDecimal;

public record ShoppingListItem(
        String ingredient,
        BigDecimal amount,
        Unit unit
) {
}
