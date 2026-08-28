package com.familymenu.backend.dish.persistence;

import com.familymenu.backend.dish.domain.Dish;
import com.familymenu.backend.dish.domain.DishIngredient;
import com.familymenu.backend.dish.domain.IngredientReference;
import com.familymenu.backend.ingredient.api.IngredientApi;
import com.familymenu.backend.ingredient.api.dto.IngredientView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class DishPersistenceService {

    private final DishRepository dishRepository;
    private final DishPersistenceMapper dishMapper;

    private final DishIngredientRepository dishIngredientRepository;
    private final DishIngredientPersistenceMapper dishIngredientMapper;

    private final IngredientApi ingredientApi;

    public void save(Dish dish) {
        dishRepository.save(dishMapper.toEntity(dish));
        saveDishIngredients(dish.getId(), dish.getIngredients());
    }

    public Optional<Dish> findById(String id) {
        return dishRepository.findById(id)
                .map(entity -> dishMapper.toDomain(entity, findDishIngredients(id)));
    }

    public void deleteById(String id) {

    }

    public List<Dish> findAll() {
        return List.of();
    }

    private void saveDishIngredients(String dishId, List<DishIngredient> ingredients) {
        List<DishIngredientEntity> entities = ingredients.stream()
                .map(ingredient -> dishIngredientMapper.toEntity(dishId, ingredient))
                .toList();

        dishIngredientRepository.saveAll(entities);
    }

    private List<DishIngredient> findDishIngredients(String dishId) {
        List<DishIngredientEntity> relationEntities =
                dishIngredientRepository.findAllByDishId(dishId);

        List<String> ingredientIds = relationEntities.stream()
                .map(DishIngredientEntity::getIngredientId)
                .distinct()
                .toList();

        Map<String, IngredientReference> ingredientsById = ingredientApi.getByIds(ingredientIds).stream()
                .collect(Collectors.toMap(
                        IngredientView::id,
                        ingredient -> new IngredientReference(ingredient.id(), ingredient.name())
                ));

        return relationEntities.stream()
                .map(entity -> dishIngredientMapper.toDomain(
                        entity,
                        requireIngredient(ingredientsById, entity.getIngredientId())
                ))
                .toList();
    }

    private IngredientReference requireIngredient(
            Map<String, IngredientReference> ingredientsById,
            String ingredientId
    ) {
        IngredientReference ingredient = ingredientsById.get(ingredientId);
        if (ingredient == null) {
            throw new IllegalStateException(
                    "Ingredient %s referenced by dish ingredient was not found".formatted(ingredientId)
            );
        }
        return ingredient;
    }

}
