package com.familymenu.backend.adapter.web.dto;

import com.familymenu.backend.domain.dish.Unit;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record IngredientRequest(

        @NotBlank
        String name,

        @NotNull
        @Positive
        BigDecimal amount,

        @NotNull
        Unit unit
) {
}