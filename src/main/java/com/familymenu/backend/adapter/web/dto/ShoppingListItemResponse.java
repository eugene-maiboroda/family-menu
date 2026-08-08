package com.familymenu.backend.adapter.web.dto;

import com.familymenu.backend.domain.Unit;

import java.math.BigDecimal;

public record ShoppingListItemResponse(
        String ingredientId,
        String name,
        BigDecimal amount,
        Unit unit
) {}