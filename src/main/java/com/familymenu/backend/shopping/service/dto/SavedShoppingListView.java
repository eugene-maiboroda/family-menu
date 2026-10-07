package com.familymenu.backend.shopping.service.dto;

import com.familymenu.backend.shopping.domain.ShoppingListItem;
import java.math.BigDecimal;
import java.util.List;

public record SavedShoppingListView(
        String id,
        String name,
        boolean inbox,
        List<String> menuIds,
        List<String> dishIds,
        List<ManualPurchaseView> manualProducts,
        List<ShoppingListItem> items
) {
    public record ManualPurchaseView(String id, String productId, BigDecimal amount, String unit) {}
}
