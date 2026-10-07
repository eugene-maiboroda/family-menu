package com.familymenu.backend.shopping.service;

import com.familymenu.backend.dish.api.DishApi;
import com.familymenu.backend.dish.api.dto.DishIngredientView;
import com.familymenu.backend.dish.api.dto.DishView;
import com.familymenu.backend.menu.api.MenuApi;
import com.familymenu.backend.menu.api.dto.MenuView;
import com.familymenu.backend.product.api.ProductApi;
import com.familymenu.backend.product.api.dto.ProductView;
import com.familymenu.backend.shopping.domain.ShoppingIngredient;
import com.familymenu.backend.shopping.domain.ShoppingListCalculator;
import com.familymenu.backend.shopping.domain.ShoppingListItem;
import com.familymenu.backend.shopping.domain.Unit;
import com.familymenu.backend.shopping.service.dto.AdditionalProduct;
import com.familymenu.backend.shopping.service.exception.ShoppingDishNotFoundException;
import com.familymenu.backend.shopping.service.exception.ShoppingMenuNotFoundException;
import com.familymenu.backend.shopping.service.exception.ShoppingProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShoppingListService {

    private final MenuApi menuApi;
    private final DishApi dishApi;
    private final ProductApi productApi;
    private final ShoppingListCalculator shoppingListCalculator;

    public List<ShoppingListItem> getShoppingList(String menuId) {
        MenuView menu = menuApi.findById(menuId)
                .orElseThrow(() -> new ShoppingMenuNotFoundException(menuId + " not found"));

        return calculateShoppingList(menu.dishIds(), List.of());
    }

    public List<ShoppingListItem> calculateShoppingList(
            List<String> dishIds,
            List<AdditionalProduct> additionalProducts
    ) {
        List<DishView> dishes = loadSelectedDishes(dishIds);
        List<ShoppingIngredient> productsToBuy = collectDishIngredients(dishes);
        productsToBuy.addAll(collectAdditionalProducts(additionalProducts));

        return shoppingListCalculator.calculateShoppingList(productsToBuy);
    }

    public List<ShoppingListItem> calculateShoppingListWithMenus(
            List<String> menuIds,
            List<String> dishIds,
            List<AdditionalProduct> additionalProducts
    ) {
        List<ShoppingIngredient> productsToBuy = collectDishIngredients(loadSelectedDishes(dishIds));

        for (String menuId : menuIds) {
            MenuView menu = menuApi.findById(menuId)
                    .orElseThrow(() -> new ShoppingMenuNotFoundException(menuId + " not found"));
            List<DishView> menuDishes = loadSelectedDishes(menu.dishIds());
            productsToBuy.addAll(collectDishIngredients(menuDishes));
        }

        productsToBuy.addAll(collectAdditionalProducts(additionalProducts));
        return shoppingListCalculator.calculateShoppingList(productsToBuy);
    }

    private List<DishView> loadSelectedDishes(List<String> dishIds) {
        List<String> uniqueDishIds = dishIds.stream()
                .distinct()
                .toList();
        List<DishView> dishes = dishApi.getByIds(uniqueDishIds);

        validateAllDishesLoaded(uniqueDishIds, dishes);
        return dishes;
    }

    private List<ShoppingIngredient> collectDishIngredients(List<DishView> dishes) {
        List<ShoppingIngredient> ingredients = new ArrayList<>();

        for (DishView dish : dishes) {
            for (DishIngredientView ingredient : dish.ingredients()) {
                ShoppingIngredient shoppingIngredient = new ShoppingIngredient(
                        ingredient.productId(),
                        ingredient.productName(),
                        ingredient.amount(),
                        parseUnit(ingredient.unit())
                );
                ingredients.add(shoppingIngredient);
            }
        }

        return ingredients;
    }

    private List<ShoppingIngredient> collectAdditionalProducts(List<AdditionalProduct> additionalProducts) {
        if (additionalProducts == null || additionalProducts.isEmpty()) {
            return List.of();
        }

        List<String> productIds = additionalProducts.stream()
                .map(AdditionalProduct::productId)
                .distinct()
                .toList();
        Map<String, ProductView> productsById = productApi.getByIds(productIds).stream()
                .collect(Collectors.toMap(ProductView::id, Function.identity()));
        List<ShoppingIngredient> additionalItems = new ArrayList<>();

        for (AdditionalProduct additionalProduct : additionalProducts) {
            ProductView product = productsById.get(additionalProduct.productId());
            if (product == null) {
                throw new ShoppingProductNotFoundException(additionalProduct.productId());
            }

            if (additionalProduct.amount() == null || additionalProduct.amount().signum() <= 0) {
                throw new IllegalArgumentException("Amount must be positive");
            }

            ShoppingIngredient shoppingIngredient = new ShoppingIngredient(
                    product.id(),
                    product.name(),
                    additionalProduct.amount(),
                    parseUnit(additionalProduct.unit())
            );
            additionalItems.add(shoppingIngredient);
        }

        return additionalItems;
    }

    private void validateAllDishesLoaded(List<String> requestedIds, List<DishView> dishes) {
        Set<String> loadedIds = dishes.stream()
                .map(DishView::id)
                .collect(Collectors.toSet());
        List<String> missingIds = requestedIds.stream()
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
            throw new IllegalArgumentException("Unsupported unit: " + unit, exception);
        }
    }
}
