package com.familymenu.backend.devguardfixture;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FixturePricingCalculator {

    private static final int BULK_DISCOUNT_MINIMUM_QUANTITY = 3;
    private static final BigDecimal BULK_PRICE_MULTIPLIER = new BigDecimal("0.90");

    /**
     * Orders of three or more units receive 10% off the entire subtotal.
     * Round the final order total to two decimal places using HALF_UP.
     */
    public BigDecimal calculateOrderTotal(BigDecimal unitPrice, int quantity) {
        if (unitPrice == null || unitPrice.signum() < 0 || unitPrice.scale() > 2) {
            throw new IllegalArgumentException("Unit price must be a nonnegative monetary amount with at most two decimals");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        BigDecimal chargedUnitPrice = unitPrice;
        if (quantity >= BULK_DISCOUNT_MINIMUM_QUANTITY) {
            chargedUnitPrice = unitPrice.multiply(BULK_PRICE_MULTIPLIER).setScale(2, RoundingMode.HALF_UP);
        }

        return chargedUnitPrice.multiply(BigDecimal.valueOf(quantity)).setScale(2, RoundingMode.HALF_UP);
    }
}
