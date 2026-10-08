package com.familymenu.backend.shopping.service;

import com.familymenu.backend.dish.api.DishApi;
import com.familymenu.backend.dish.api.dto.DishIngredientView;
import com.familymenu.backend.dish.api.dto.DishView;
import com.familymenu.backend.menu.api.MenuApi;
import com.familymenu.backend.menu.api.dto.MenuView;
import com.familymenu.backend.shopping.domain.ShoppingListItem;
import com.familymenu.backend.shopping.domain.ShoppingListCalculator;
import com.familymenu.backend.shopping.domain.Unit;
import com.familymenu.backend.shopping.service.exception.ShoppingDishNotFoundException;
import com.familymenu.backend.shopping.service.exception.ShoppingMenuNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ShoppingListServiceTest {

    private InMemoryMenuApi menuApi;
    private InMemoryDishApi dishApi;
    private ShoppingListService service;

    @BeforeEach
    void setUp() {
        menuApi = new InMemoryMenuApi();
        dishApi = new InMemoryDishApi();
        service = new ShoppingListService(menuApi, dishApi, new com.familymenu.backend.product.api.ProductApi() {
            public Optional<com.familymenu.backend.product.api.dto.ProductView> findById(String id) {
                return Optional.of(new com.familymenu.backend.product.api.dto.ProductView(id, id));
            }
            public com.familymenu.backend.product.api.dto.ProductView resolveByName(String name) { throw new UnsupportedOperationException(); }
            public List<com.familymenu.backend.product.api.dto.ProductView> getByIds(java.util.Collection<String> ids) { return ids.stream().map(id -> new com.familymenu.backend.product.api.dto.ProductView(id, id)).toList(); }
        }, new ShoppingListCalculator());
    }

    @Test
    void buildsShoppingListThroughMenuAndDishApis() {
        menuApi.menus.put("menu-1", new MenuView(
                "menu-1",
                "Weekly menu",
                Instant.parse("2026-08-28T09:00:00Z"),
                List.of("dish-1", "dish-2")
        ));
        dishApi.dishes.put("dish-1", dish("dish-1", ingredient("tomato", "Tomato", "500", "G")));
        dishApi.dishes.put("dish-2", dish("dish-2", ingredient("tomato", "Tomato", "700", "G")));

        List<ShoppingListItem> result = service.calculateShoppingListWithMenus(List.of("menu-1"), List.of(), List.of());

        assertThat(result).containsExactly(
                new ShoppingListItem("tomato", "Tomato", new BigDecimal("1.2"), Unit.KG)
        );
    }

    @Test
    void rejectsUnknownMenu() {
        assertThatThrownBy(() -> service.calculateShoppingListWithMenus(List.of("missing-menu"), List.of(), List.of()))
                .isInstanceOf(ShoppingMenuNotFoundException.class)
                .hasMessageContaining("missing-menu");
    }

    @Test
    void rejectsIncompleteDishReadFromModuleApi() {
        menuApi.menus.put("menu-1", new MenuView(
                "menu-1",
                "Weekly menu",
                Instant.parse("2026-08-28T09:00:00Z"),
                List.of("dish-1", "missing-dish")
        ));
        dishApi.dishes.put("dish-1", dish("dish-1", ingredient("tomato", "Tomato", "500", "G")));

        assertThatThrownBy(() -> service.calculateShoppingListWithMenus(List.of("menu-1"), List.of(), List.of()))
                .isInstanceOf(ShoppingDishNotFoundException.class)
                .hasMessageContaining("missing-dish");
    }

    private DishView dish(String id, DishIngredientView ingredient) {
        return new DishView(id, id, "DINNER", List.of(ingredient), List.of());
    }

    private DishIngredientView ingredient(String id, String name, String amount, String unit) {
        return new DishIngredientView(id, name, new BigDecimal(amount), unit);
    }

    private static final class InMemoryMenuApi implements MenuApi {

        private final Map<String, MenuView> menus = new HashMap<>();

        @Override
        public Optional<MenuView> findById(String id) {
            return Optional.ofNullable(menus.get(id));
        }
    }

    private static final class InMemoryDishApi implements DishApi {

        private final Map<String, DishView> dishes = new HashMap<>();

        @Override
        public List<DishView> getByIds(Collection<String> ids) {
            return ids.stream()
                    .map(dishes::get)
                    .flatMap(java.util.stream.Stream::ofNullable)
                    .toList();
        }
    }
}
