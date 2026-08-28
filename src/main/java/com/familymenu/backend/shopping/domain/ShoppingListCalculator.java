package com.familymenu.backend.shopping.domain;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ShoppingListCalculator {

    public List<ShoppingListItem> calculate(List<ShoppingIngredient> ingredients) {
        Map<AggregationKey, AggregatedIngredient> aggregated = new LinkedHashMap<>();

        for (ShoppingIngredient ingredient : ingredients) {
            AggregationKey key = new AggregationKey(
                    ingredient.ingredientId(),
                    ingredient.unit().dimension()
            );
            BigDecimal baseAmount = ingredient.unit().toBase(ingredient.amount());
            aggregated.compute(key, (ignored, current) -> current == null
                    ? new AggregatedIngredient(ingredient.ingredientName(), baseAmount)
                    : current.add(baseAmount));
        }

        return aggregated.entrySet().stream()
                .map(entry -> toShoppingListItem(entry.getKey(), entry.getValue()))
                .toList();
    }

    private ShoppingListItem toShoppingListItem(
            AggregationKey key,
            AggregatedIngredient ingredient
    ) {
        Unit displayUnit = Unit.displayUnit(key.dimension(), ingredient.baseAmount());
        return new ShoppingListItem(
                key.ingredientId(),
                ingredient.name(),
                displayUnit.fromBase(ingredient.baseAmount()),
                displayUnit
        );
    }

    private record AggregationKey(String ingredientId, Unit.Dimension dimension) {
    }

    private record AggregatedIngredient(String name, BigDecimal baseAmount) {

        private AggregatedIngredient add(BigDecimal amount) {
            return new AggregatedIngredient(name, baseAmount.add(amount));
        }
    }
}
