package com.familymenu.backend.domain.shopping;

import com.familymenu.backend.domain.dish.Unit;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ShoppingListItem(

        String ingredientId,

        String ingredientName,

        BigDecimal amount,

        Unit unit
) {
}
