package com.familymenu.backend.shopping.domain;

public record PurchasedItem(String productId, Unit.Dimension dimension) {

    public boolean matches(ShoppingListItem item) {
        return productId.equals(item.productId()) && dimension == item.unit().dimension();
    }
}
