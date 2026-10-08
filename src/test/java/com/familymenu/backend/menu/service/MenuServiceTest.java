package com.familymenu.backend.menu.service;

import com.familymenu.backend.menu.api.dto.MenuView;

import com.familymenu.backend.dish.api.DishApi;
import com.familymenu.backend.dish.api.dto.DishView;
import com.familymenu.backend.menu.domain.Menu;
import com.familymenu.backend.menu.persistence.MenuPersistenceService;
import com.familymenu.backend.menu.service.dto.CreateMenuCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuServiceTest {

    private InMemoryMenuPersistence menuPersistence;
    private InMemoryDishApi dishApi;
    private MenuService menuService;

    @BeforeEach
    void setUp() {
        menuPersistence = new InMemoryMenuPersistence();
        dishApi = new InMemoryDishApi();
        menuService = new MenuService(menuPersistence, dishApi, event -> {});
    }

    @Test
    void createMenuValidatesDishesAndPersistsTheirIds() {
        dishApi.add("dish-1", "dish-2");

        Menu menu = menuService.createMenu(new CreateMenuCommand("  Weekly menu  ", List.of("dish-1", "dish-2")));

        assertThat(menu.id()).isNotBlank();
        assertThat(menu.name()).isEqualTo("Weekly menu");
        assertThat(menu.createdDate()).isNotNull();
        assertThat(menu.dishIds()).containsExactly("dish-1", "dish-2");
        assertThat(menuPersistence.savedMenus).containsExactly(menu);
    }

    @Test
    void createMenuRejectsMissingDishReferences() {
        dishApi.add("dish-1");

        assertThatThrownBy(() -> menuService.createMenu(
                new CreateMenuCommand("  Weekly menu  ", List.of("dish-1", "missing-dish"))
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("missing-dish");

        assertThat(menuPersistence.savedMenus).isEmpty();
    }

    @Test
    void moduleApiReturnsMenuViewInsteadOfMenuDomain() {
        Menu menu = new Menu("menu-1", "Weekly menu", java.time.Instant.parse("2026-08-28T09:00:00Z"), List.of("dish-1"));
        menuPersistence.menusById.put(menu.id(), menu);

        Optional<MenuView> result = menuService.findById("menu-1");

        assertThat(result).hasValueSatisfying(view -> {
            assertThat(view.id()).isEqualTo("menu-1");
            assertThat(view.name()).isEqualTo("Weekly menu");
            assertThat(view.createdDate()).isEqualTo(menu.createdDate());
            assertThat(view.dishIds()).containsExactly("dish-1");
        });
    }

    @Test
    void forwardsSearchQueryAndAcceptsMissingQuery() {
        assertThat(menuService.getMenus("week")).isEmpty();
        assertThat(menuPersistence.receivedQuery).isEqualTo("week");
        assertThat(menuService.getMenus(null)).isEmpty();
        assertThat(menuPersistence.receivedQuery).isNull();
    }

    @Test
    void rejectsBlankMenuName() {
        dishApi.add("dish-1");
        assertThatThrownBy(() -> menuService.createMenu(new CreateMenuCommand(" ", List.of("dish-1"))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Menu name is required");
        assertThat(menuPersistence.savedMenus).isEmpty();
    }

    private static final class InMemoryMenuPersistence extends MenuPersistenceService {

        private final Map<String, Menu> menusById = new HashMap<>();
        private final List<Menu> savedMenus = new ArrayList<>();
        private String receivedQuery;

        private InMemoryMenuPersistence() {
            super(null, null);
        }

        @Override
        public Menu saveMenu(Menu menu) {
            savedMenus.add(menu);
            menusById.put(menu.id(), menu);
            return menu;
        }

        @Override
        public void deleteMenu(String id) {
            menusById.remove(id);
        }

        @Override
        public Optional<Menu> findMenuById(String id) {
            return Optional.ofNullable(menusById.get(id));
        }

        @Override
        public List<Menu> findAllMenus(String query) {
            receivedQuery = query;
            return List.copyOf(menusById.values());
        }
    }

    private static final class InMemoryDishApi implements DishApi {

        private final Map<String, DishView> dishesById = new HashMap<>();

        private void add(String... ids) {
            for (String id : ids) {
                dishesById.put(id, new DishView(id, id, "DINNER", List.of(), List.of()));
            }
        }

        @Override
        public List<DishView> getByIds(Collection<String> ids) {
            return ids.stream()
                    .map(dishesById::get)
                    .flatMap(java.util.stream.Stream::ofNullable)
                    .toList();
        }
    }
}
