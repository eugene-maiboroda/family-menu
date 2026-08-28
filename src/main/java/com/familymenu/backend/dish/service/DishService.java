package com.familymenu.backend.dish.service;

import com.familymenu.backend.dish.api.DishApi;
import com.familymenu.backend.dish.api.dto.DishIngredientView;
import com.familymenu.backend.dish.api.dto.DishView;
import com.familymenu.backend.dish.api.dto.RecipeStepView;
import com.familymenu.backend.dish.domain.Dish;
import com.familymenu.backend.dish.domain.DishIngredient;
import com.familymenu.backend.dish.domain.IngredientReference;
import com.familymenu.backend.dish.persistence.DishPersistenceService;
import com.familymenu.backend.ingredient.api.IngredientApi;
import com.familymenu.backend.ingredient.api.dto.IngredientView;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Collection;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DishService implements DishApi {

    private final DishPersistenceService dishPersistence;
    private final IngredientApi ingredientApi;

    @Transactional
    public void createDish(Dish dish) {
        List<DishIngredient> resolvedIngredients = dish.getIngredients().stream()
                .map(this::resolveIngredient)
                .toList();

        Dish dishForSave = dish.toBuilder()
                .id(UUID.randomUUID().toString())
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

    public void deleteDish(String id) {

    }

    public List<Dish> getDishes() {
        return List.of();
    }

    public Dish getDish(String id) {
        return null;
    }

    @Override
    public List<DishView> getByIds(Collection<String> ids) {
        return ids.stream()
                .map(dishPersistence::findById)
                .flatMap(java.util.Optional::stream)
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
        return dishPersistence.findById(id)
                .orElseThrow(() -> new DishNotFoundException(id + " not found"));
    }

    private DishView toView(Dish dish) {
        return new DishView(
                dish.getId(),
                dish.getName(),
                dish.getCategory().name(),
                dish.getIngredients().stream()
                        .map(ingredient -> new DishIngredientView(
                                ingredient.ingredient().id(),
                                ingredient.ingredient().name(),
                                ingredient.amount(),
                                ingredient.unit().name()
                        ))
                        .toList(),
                dish.getRecipeSteps().stream()
                        .map(step -> new RecipeStepView(step.description(), step.imageUrl()))
                        .toList()
        );
    }
}
