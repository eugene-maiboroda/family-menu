package com.familymenu.backend.shopping.domain;

import java.math.BigDecimal;

public record ManualProductPurchase(
        String id,
        String productId,
        BigDecimal amount,
        Unit unit
) {
}
