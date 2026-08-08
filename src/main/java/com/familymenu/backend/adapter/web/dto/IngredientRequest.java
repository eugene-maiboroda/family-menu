package com.familymenu.backend.adapter.web.dto;

import com.familymenu.backend.domain.Unit;

import java.math.BigDecimal;

public record IngredientRequest(
        String name,
        BigDecimal amount,
        Unit unit
) {
}