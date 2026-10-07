package com.familymenu.backend.shopping.domain;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class ShoppingListCalculator {

    public List<ShoppingListItem> calculateShoppingList(List<ShoppingIngredient> ingredients) {
        Map<ProductMeasurement, ProductTotal> productTotals = new LinkedHashMap<>();

        for (ShoppingIngredient ingredient : ingredients) {
            ProductMeasurement productMeasurement = new ProductMeasurement(
                    ingredient.productId(),
                    ingredient.unit().dimension()
            );

            // Sum compatible quantities in grams, milliliters or pieces.
            BigDecimal amountInBaseUnit = ingredient.unit().toBase(ingredient.amount());
            ProductTotal existingTotal = productTotals.get(productMeasurement);
            BigDecimal totalAmountInBaseUnit = amountInBaseUnit;
            String productName = ingredient.productName();

            if (existingTotal != null) {
                totalAmountInBaseUnit = existingTotal.amountInBaseUnit().add(amountInBaseUnit);
                productName = existingTotal.productName();
            }

            ProductTotal updatedTotal = new ProductTotal(productName, totalAmountInBaseUnit);
            productTotals.put(productMeasurement, updatedTotal);
        }

        return productTotals.entrySet().stream()
                .map(entry -> toShoppingListItem(entry.getKey(), entry.getValue()))
                .toList();
    }

    private ShoppingListItem toShoppingListItem(ProductMeasurement productMeasurement, ProductTotal productTotal) {
        Unit displayUnit = Unit.displayUnit(
                productMeasurement.dimension(),
                productTotal.amountInBaseUnit()
        );
        BigDecimal amountToBuy = displayUnit.fromBase(productTotal.amountInBaseUnit());

        return new ShoppingListItem(
                productMeasurement.productId(),
                productTotal.productName(),
                amountToBuy,
                displayUnit
        );
    }

    // The same product measured in pieces and grams must remain separate shopping items.
    private record ProductMeasurement(String productId, Unit.Dimension dimension) {
    }

    private record ProductTotal(String productName, BigDecimal amountInBaseUnit) {
    }
}
