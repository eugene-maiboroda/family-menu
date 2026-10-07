package com.familymenu.backend.shopping.web.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record ShoppingListProductRequest(
        @NotBlank String productId,
        @NotNull @Positive @Digits(integer = 8, fraction = 2) BigDecimal amount,
        @NotBlank String unit
) {}
