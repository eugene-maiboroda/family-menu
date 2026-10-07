package com.familymenu.backend.dish.service;

import com.familymenu.backend.dish.api.DishApi;
import com.familymenu.backend.dish.api.dto.DishIngredientView;
import com.familymenu.backend.dish.api.dto.DishView;
import com.familymenu.backend.dish.api.dto.RecipeStepView;
import com.familymenu.backend.dish.domain.Dish;
import com.familymenu.backend.dish.domain.DishIngredient;
import com.familymenu.backend.dish.domain.IngredientReference;
import com.familymenu.backend.dish.domain.RecipeStep;
import com.familymenu.backend.dish.persistence.DishPersistenceService;
import com.familymenu.backend.ingredient.api.IngredientApi;
import com.familymenu.backend.ingredient.api.dto.IngredientView;
import com.familymenu.backend.shared.IdGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DishService implements DishApi {

    private final DishPersistenceService dishPersistence;
    private final IngredientApi ingredientApi;
    private final IdGenerator idGenerator;

    @Transactional
    public void createDish(Dish dish) {
        List<DishIngredient> resolvedIngredients = dish.getIngredients().stream()
                .map(this::resolveIngredient)
                .toList();

        Dish dishForSave = dish.toBuilder()
                .id(idGenerator.generate())
                .ingredients(resolvedIngredients)
                .build();

        dishPersistence.save(dishForSave);
    }

    @Transactional
    public Dish updateDish(Dish changes) {

        Dish existing = findOrThrow(changes.getId());

        Dish updated = existing.toBuilder()
                .name(changes.getName())
                .category(changes.getCategory())
                .recipeSteps(changes.getRecipeSteps())
                .build();

        dishPersistence.save(updated);

        return updated;
    }

    @Transactional
    public void deleteDish(String id) {
        dishPersistence.deleteById(id);
    }

    public List<Dish> listDishes() {
        return dishPersistence.findAll().stream()
                .map(this::resolveIngredientReferences)
                .toList();
    }

    public Dish getDish(String id) {
        return findOrThrow(id);
    }

    @Override
    public List<DishView> getByIds(Collection<String> ids) {
        return ids.stream()
                .map(this::findById)
                .flatMap(Optional::stream)
                .map(this::toView)
                .toList();
    }

    private DishIngredient resolveIngredient(DishIngredient dishIngredient) {
        IngredientView ingredient = ingredientApi.resolveByName(dishIngredient.ingredient().name());

        return dishIngredient.toBuilder()
                .id(UUID.randomUUID().toString())
                .ingredient(new IngredientReference(ingredient.id(), ingredient.name()))
                .build();
    }

    private Dish findOrThrow(String id) {
        return findById(id)
                .orElseThrow(() -> new DishNotFoundException(id + " not found"));
    }

    private Optional<Dish> findById(String id) {
        return dishPersistence.findById(id)
                .map(this::resolveIngredientReferences);
    }

    private Dish resolveIngredientReferences(Dish dish) {
        List<String> ingredientIds = dish.getIngredients().stream()
                .map(ingredient -> ingredient.ingredient().id())
                .distinct()
                .toList();

        Map<String, IngredientReference> ingredientsById = ingredientApi.getByIds(ingredientIds).stream()
                .collect(Collectors.toMap(
                        IngredientView::id,
                        ingredient -> new IngredientReference(ingredient.id(), ingredient.name())
                ));

        List<DishIngredient> resolvedIngredients = dish.getIngredients().stream()
                .map(ingredient -> ingredient.toBuilder()
                        .ingredient(requireIngredient(ingredientsById, ingredient.ingredient().id()))
                        .build())
                .toList();

        return dish.toBuilder()
                .ingredients(resolvedIngredients)
                .build();
    }

    private IngredientReference requireIngredient(Map<String, IngredientReference> ingredientsById, String ingredientId) {
        IngredientReference ingredient = ingredientsById.get(ingredientId);
        if (ingredient == null) {
            throw new IllegalStateException(
                    "Ingredient %s referenced by dish ingredient was not found".formatted(ingredientId)
            );
        }
        return ingredient;
    }

    private DishView toView(Dish dish) {
        List<DishIngredientView> allIngredients = dish.getIngredients().stream()
                .map(this::dishToIngredientView)
                .toList();

        List<RecipeStepView> allSteps = dish.getRecipeSteps().stream()
                .map(step -> new RecipeStepView(step.description(), step.imageUrl()))
                .toList();

        return new DishView(dish.getId(), dish.getName(), dish.getCategory().name(), allIngredients, allSteps);
    }

    private DishIngredientView dishToIngredientView(DishIngredient ingredient) {
        return new DishIngredientView(
                ingredient.ingredient().id(),
                ingredient.ingredient().name(),
                ingredient.amount(),
                ingredient.unit().name()
        );
    }
}
