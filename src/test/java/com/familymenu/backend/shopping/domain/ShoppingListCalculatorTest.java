package com.familymenu.backend.shopping.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ShoppingListCalculatorTest {

    private final ShoppingListCalculator calculator = new ShoppingListCalculator();

    @Test
    void aggregatesMassAndDisplaysKilogramsFromOneThousandGrams() {
        List<ShoppingListItem> result = calculator.calculate(List.of(
                ingredient("tomato", "Tomato", "500", Unit.G),
                ingredient("tomato", "Tomato", "0.7", Unit.KG)
        ));

        assertThat(result).containsExactly(item("tomato", "Tomato", "1.2", Unit.KG));
    }

    @Test
    void keepsMassAndVolumeBelowThresholdInBaseUnits() {
        List<ShoppingListItem> result = calculator.calculate(List.of(
                ingredient("milk", "Milk", "500", Unit.ML),
                ingredient("milk", "Milk", "0.25", Unit.L),
                ingredient("flour", "Flour", "250", Unit.G)
        ));

        assertThat(result).containsExactly(
                item("milk", "Milk", "750", Unit.ML),
                item("flour", "Flour", "250", Unit.G)
        );
    }

    @Test
    void displaysLitersAndRemovesTrailingZeros() {
        List<ShoppingListItem> result = calculator.calculate(List.of(
                ingredient("milk", "Milk", "1", Unit.L),
                ingredient("milk", "Milk", "200", Unit.ML)
        ));

        assertThat(result).containsExactly(item("milk", "Milk", "1.2", Unit.L));
        assertThat(result.getFirst().amount().toPlainString()).isEqualTo("1.2");
    }

    @Test
    void neverMixesIncompatibleDimensionsForTheSameIngredient() {
        List<ShoppingListItem> result = calculator.calculate(List.of(
                ingredient("lemon", "Lemon", "2", Unit.PCS),
                ingredient("lemon", "Lemon", "100", Unit.ML),
                ingredient("lemon", "Lemon", "0.2", Unit.L)
        ));

        assertThat(result).containsExactly(
                item("lemon", "Lemon", "2", Unit.PCS),
                item("lemon", "Lemon", "300", Unit.ML)
        );
    }

    @Test
    void keepsWholeCountInPlainDecimalNotation() {
        List<ShoppingListItem> result = calculator.calculate(List.of(
                ingredient("egg", "Egg", "1000", Unit.PCS)
        ));

        assertThat(result.getFirst().amount().toPlainString()).isEqualTo("1000");
        assertThat(result.getFirst().unit()).isEqualTo(Unit.PCS);
    }

    private ShoppingIngredient ingredient(String id, String name, String amount, Unit unit) {
        return new ShoppingIngredient(id, name, new BigDecimal(amount), unit);
    }

    private ShoppingListItem item(String id, String name, String amount, Unit unit) {
        return new ShoppingListItem(id, name, new BigDecimal(amount), unit);
    }
}
