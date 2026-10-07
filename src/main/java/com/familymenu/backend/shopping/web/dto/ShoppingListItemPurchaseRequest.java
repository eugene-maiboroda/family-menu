package com.familymenu.backend.shopping.web.dto;

import jakarta.validation.constraints.NotNull;

public record ShoppingListItemPurchaseRequest(@NotNull Boolean purchased) {
}
