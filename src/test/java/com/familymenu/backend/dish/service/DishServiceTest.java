package com.familymenu.backend.dish.service;

import com.familymenu.backend.dish.api.dto.DishView;
import com.familymenu.backend.dish.domain.Dish;
import com.familymenu.backend.dish.domain.DishCategory;
import com.familymenu.backend.dish.domain.DishIngredient;
import com.familymenu.backend.dish.domain.IngredientReference;
import com.familymenu.backend.dish.domain.RecipeStep;
import com.familymenu.backend.dish.domain.Unit;
import com.familymenu.backend.dish.persistence.DishPersistenceService;
import com.familymenu.backend.ingredient.api.IngredientApi;
import com.familymenu.backend.ingredient.api.dto.IngredientView;
import com.familymenu.backend.shared.IdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class DishServiceTest {

    private InMemoryDishPersistence dishPersistence;
    private InMemoryIngredientApi ingredientApi;
    private DishService dishService;
    private final IdGenerator idGenerator = new IdGenerator() {
        private int counter = 0;

        @Override
        public String generate() {
            counter++;
            return "dish-" + counter;
        }
    };

    @BeforeEach
    void setUp() {
        dishPersistence = new InMemoryDishPersistence();
        ingredientApi = new InMemoryIngredientApi();
        dishService = new DishService(dishPersistence, ingredientApi, idGenerator);
    }

    @Test
    void createDishReusesCanonicalIngredientAndAssignsDishAndRelationIds() {
        IngredientView canonicalTomato = new IngredientView("ingredient-1", "Tomato");
        ingredientApi.ingredientsByName.put("Tomato", canonicalTomato);
        Dish dish = dishWithoutId(
                "Pasta",
                List.of(new DishIngredient(null, new IngredientReference(null, "Tomato"), new BigDecimal("500"), Unit.G))
        );

        dishService.createDish(dish);

        Dish savedDish = dishPersistence.savedDishes.getFirst();
        assertThat(savedDish.getId()).isNotBlank();
        assertThat(savedDish.getIngredients()).singleElement().satisfies(ingredient -> {
            assertThat(ingredient.id()).isNotBlank();
            assertThat(ingredient.ingredient())
                    .isEqualTo(new IngredientReference(canonicalTomato.id(), canonicalTomato.name()));
            assertThat(ingredient.amount()).isEqualByComparingTo("500");
            assertThat(ingredient.unit()).isEqualTo(Unit.G);
        });
        assertThat(ingredientApi.createCalls).isZero();
    }

    @Test
    void createDishCreatesCanonicalIngredientWhenItDoesNotExist() {
        Dish dish = dishWithoutId(
                "Salad",
                List.of(new DishIngredient(null, new IngredientReference(null, "Cucumber"), new BigDecimal("2"), Unit.PCS))
        );

        dishService.createDish(dish);

        assertThat(ingredientApi.createCalls).isOne();
        assertThat(ingredientApi.ingredientsByName.get("Cucumber"))
                .isEqualTo(new IngredientView("ingredient-1", "Cucumber"));
        assertThat(dishPersistence.savedDishes.getFirst().getIngredients()).singleElement().satisfies(ingredient -> {
            assertThat(ingredient.id()).isNotBlank();
            assertThat(ingredient.ingredient()).isEqualTo(new IngredientReference("ingredient-1", "Cucumber"));
            assertThat(ingredient.amount()).isEqualByComparingTo("2");
            assertThat(ingredient.unit()).isEqualTo(Unit.PCS);
        });
    }

    @Test
    void updateDishChangesEditableFieldsButKeepsExistingIngredients() {
        DishIngredient existingIngredient = new DishIngredient(
                "dish-ingredient-1",
                new IngredientReference("ingredient-1", "Tomato"),
                new BigDecimal("500"),
                Unit.G
        );
        Dish existing = Dish.builder()
                .id("dish-1")
                .name("Old name")
                .category(DishCategory.LUNCH)
                .recipeSteps(List.of(new RecipeStep("Old step", null)))
                .ingredients(List.of(existingIngredient))
                .build();
        Dish changes = Dish.builder()
                .id("dish-1")
                .name("New name")
                .category(DishCategory.DINNER)
                .recipeSteps(List.of(new RecipeStep("New step", "image-key")))
                .ingredients(List.of())
                .build();
        dishPersistence.dishesById.put("dish-1", existing);
        ingredientApi.ingredientsByName.put("Tomato", new IngredientView("ingredient-1", "Tomato"));

        Dish updated = dishService.updateDish(changes);

        assertThat(updated.getName()).isEqualTo("New name");
        assertThat(updated.getCategory()).isEqualTo(DishCategory.DINNER);
        assertThat(updated.getRecipeSteps()).containsExactly(new RecipeStep("New step", "image-key"));
        assertThat(updated.getIngredients()).containsExactly(existingIngredient);
        assertThat(dishPersistence.savedDishes).containsExactly(updated);
    }

    @Test
    void moduleApiReturnsBoundaryDtoInsteadOfDishDomain() {
        Dish dish = Dish.builder()
                .id("dish-1")
                .name("Pasta")
                .category(DishCategory.DINNER)
                .ingredients(List.of(new DishIngredient(
                        "dish-ingredient-1",
                        new IngredientReference("ingredient-1", "Tomato"),
                        new BigDecimal("500"),
                        Unit.G
                )))
                .recipeSteps(List.of(new RecipeStep("Cook", "image-key")))
                .build();

        dishPersistence.dishesById.put(dish.getId(), dish);
        ingredientApi.ingredientsByName.put("Tomato", new IngredientView("ingredient-1", "Tomato"));

        List<DishView> views = dishService.getByIds(List.of("dish-1", "missing-dish"));

        assertThat(views).singleElement().satisfies(view -> {
            assertThat(view.id()).isEqualTo("dish-1");
            assertThat(view.category()).isEqualTo("DINNER");
            assertThat(view.ingredients()).singleElement().satisfies(ingredient -> {
                assertThat(ingredient.ingredientId()).isEqualTo("ingredient-1");
                assertThat(ingredient.ingredientName()).isEqualTo("Tomato");
                assertThat(ingredient.amount()).isEqualByComparingTo("500");
                assertThat(ingredient.unit()).isEqualTo("G");
            });
            assertThat(view.recipeSteps()).singleElement().satisfies(step -> {
                assertThat(step.description()).isEqualTo("Cook");
                assertThat(step.imageUrl()).isEqualTo("image-key");
            });
        });
    }

    private Dish dishWithoutId(String name, List<DishIngredient> ingredients) {
        return Dish.builder()
                .name(name)
                .category(DishCategory.DINNER)
                .recipeSteps(List.of())
                .ingredients(ingredients)
                .build();
    }

    private static final class InMemoryDishPersistence extends DishPersistenceService {

        private final Map<String, Dish> dishesById = new HashMap<>();
        private final List<Dish> savedDishes = new ArrayList<>();

        private InMemoryDishPersistence() {
            super(null, null, null, null);
        }

        @Override
        public void save(Dish dish) {
            savedDishes.add(dish);
            dishesById.put(dish.getId(), dish);
        }

        @Override
        public Optional<Dish> findById(String id) {
            return Optional.ofNullable(dishesById.get(id));
        }

        @Override
        public void deleteById(String id) {
            dishesById.remove(id);
        }

        @Override
        public List<Dish> findAll() {
            return List.copyOf(dishesById.values());
        }
    }

    private static final class InMemoryIngredientApi implements IngredientApi {

        private final Map<String, IngredientView> ingredientsByName = new HashMap<>();
        private int createCalls;

        @Override
        public IngredientView resolveByName(String name) {
            IngredientView existing = ingredientsByName.get(name);
            if (existing != null) {
                return existing;
            }

            createCalls++;
            IngredientView created = new IngredientView("ingredient-" + createCalls, name);
            ingredientsByName.put(name, created);
            return created;
        }

        @Override
        public List<IngredientView> getByIds(java.util.Collection<String> ids) {
            return ingredientsByName.values().stream()
                    .filter(ingredient -> ids.contains(ingredient.id()))
                    .toList();
        }
    }

}
