package com.familymenu.backend.adapter.web.dto;

import com.familymenu.backend.domain.Unit;

import java.math.BigDecimal;

public record IngredientResponse(
        String id,
        String name,
        BigDecimal amount,
        Unit unit
) {
}