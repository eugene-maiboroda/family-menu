package com.familymenu.backend.dish.web.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record IngredientRequest(

        String productId,

        @Size(max = 255) String name,

        @NotNull @Positive @Digits(integer = 8, fraction = 2) BigDecimal amount,

        @NotNull String unit
) {
    @AssertTrue(message = "Provide either productId or a new product name")
    public boolean isProductSpecified() {
        return productId != null && !productId.isBlank() && name == null
                || productId == null && name != null && !name.isBlank();
    }
}
