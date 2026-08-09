package com.familymenu.backend.application;

import com.familymenu.backend.domain.dish.Dish;
import com.familymenu.backend.domain.dish.DishIngredient;
import com.familymenu.backend.domain.dish.Ingredient;
import com.familymenu.backend.infrastructure.exception.DishNotFoundException;
import com.familymenu.backend.port.in.DishUseCase;
import com.familymenu.backend.port.out.DishPersistencePort;
import com.familymenu.backend.port.out.IdGenerationPort;
import com.familymenu.backend.port.out.IngredientPersistencePort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DishService implements DishUseCase {

    private final DishPersistencePort dishPort;
    private final IngredientPersistencePort ingredientPort;
    private final IdGenerationPort idGeneration;

    @Override
    @Transactional
    public void createDish(Dish dish) {
        List<DishIngredient> resolvedIngredients = dish.getIngredients().stream()
                .map(this::resolveIngredient)
                .toList();

        Dish dishForSave = dish.toBuilder()
                .id(idGeneration.generateId())
                .ingredients(resolvedIngredients)
                .build();

        dishPort.save(dishForSave);
        dishPort.saveDishIngredients(dishForSave.getId(), resolvedIngredients);


    }

    @Override
    public Dish updateDish(Dish changes) {

        Dish existing = findOrThrow(changes.getId());

        Dish updated = existing.toBuilder()
                .name(changes.getName())
                .category(changes.getCategory())
                .recipeSteps(changes.getRecipeSteps())
                .build();

        dishPort.save(updated);

        return updated;
    }

    @Override
    public void deleteDish(String id) {

    }

    @Override
    public List<Dish> getDishes() {
        return List.of();
    }

    @Override
    public Dish getDish(String id) {
        return null;
    }

    private DishIngredient resolveIngredient(DishIngredient dishIngredient) {
        Ingredient ingredient = ingredientPort.findByName(dishIngredient.ingredient().name())
                .orElseGet(() -> {
                    Ingredient newIngredient = dishIngredient.ingredient().toBuilder()
                            .id(idGeneration.generateId())
                            .build();
                    return ingredientPort.save(newIngredient);
                });

        return dishIngredient.toBuilder()
                .id(idGeneration.generateId())
                .ingredient(ingredient)
                .build();
    }

    private Dish findOrThrow(String id) {
        return dishPort.findById(id)
                .orElseThrow(() -> new DishNotFoundException(id + " not found"));
    }
}
