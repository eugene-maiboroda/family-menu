package com.familymenu.backend.shopping.service;

import com.familymenu.backend.dish.api.DishApi;
import com.familymenu.backend.dish.api.dto.DishView;
import com.familymenu.backend.menu.api.MenuApi;
import com.familymenu.backend.menu.api.dto.MenuView;
import com.familymenu.backend.shopping.domain.ShoppingIngredient;
import com.familymenu.backend.shopping.domain.ShoppingListCalculator;
import com.familymenu.backend.shopping.domain.ShoppingListItem;
import com.familymenu.backend.shopping.domain.Unit;
import com.familymenu.backend.shopping.service.exception.ShoppingDishNotFoundException;
import com.familymenu.backend.shopping.service.exception.ShoppingMenuNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShoppingListService {

    private final MenuApi menuApi;
    private final DishApi dishApi;
    private final ShoppingListCalculator calculator;

    public List<ShoppingListItem> getShoppingList(String menuId) {
        MenuView menu = menuApi.findById(menuId)
                .orElseThrow(() -> new ShoppingMenuNotFoundException(menuId + " not found"));
        List<DishView> dishes = dishApi.getByIds(menu.dishIds());
        validateAllDishesLoaded(menu.dishIds(), dishes);

        List<ShoppingIngredient> ingredients = dishes.stream()
                .flatMap(dish -> dish.ingredients().stream())
                .map(ingredient -> new ShoppingIngredient(
                        ingredient.ingredientId(),
                        ingredient.ingredientName(),
                        ingredient.amount(),
                        parseUnit(ingredient.unit())
                ))
                .toList();

        return calculator.calculate(ingredients);
    }

    private void validateAllDishesLoaded(List<String> requestedIds, List<DishView> dishes) {
        HashSet<String> loadedIds = dishes.stream()
                .map(DishView::id)
                .collect(java.util.stream.Collectors.toCollection(HashSet::new));
        List<String> missingIds = requestedIds.stream()
                .distinct()
                .filter(id -> !loadedIds.contains(id))
                .toList();
        if (!missingIds.isEmpty()) {
            throw new ShoppingDishNotFoundException("Dishes not found: " + missingIds);
        }
    }

    private Unit parseUnit(String unit) {
        try {
            return Unit.valueOf(unit);
        } catch (IllegalArgumentException exception) {
            throw new IllegalStateException("Unsupported dish ingredient unit: " + unit, exception);
        }
    }
}
