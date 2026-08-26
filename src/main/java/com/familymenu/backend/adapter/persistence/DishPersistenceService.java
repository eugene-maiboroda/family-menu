package com.familymenu.backend.adapter.persistence;

import com.familymenu.backend.adapter.persistence.entity.DishIngredientEntity;
import com.familymenu.backend.adapter.persistence.mapper.DishIngredientPersistenceMapper;
import com.familymenu.backend.adapter.persistence.mapper.DishPersistenceMapper;
import com.familymenu.backend.adapter.persistence.mapper.IngredientPersistenceMapper;
import com.familymenu.backend.adapter.persistence.repository.DishIngredientRepository;
import com.familymenu.backend.adapter.persistence.repository.DishRepository;
import com.familymenu.backend.adapter.persistence.repository.IngredientRepository;
import com.familymenu.backend.domain.dish.Dish;
import com.familymenu.backend.domain.dish.DishIngredient;
import com.familymenu.backend.domain.dish.Ingredient;
import com.familymenu.backend.port.out.DishPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class DishPersistenceService implements DishPersistencePort {

    private final DishRepository dishRepository;
    private final DishPersistenceMapper dishMapper;

    private final DishIngredientRepository dishIngredientRepository;
    private final DishIngredientPersistenceMapper dishIngredientMapper;

    private final IngredientRepository ingredientRepository;
    private final IngredientPersistenceMapper ingredientMapper;

    @Override
    public void save(Dish dish) {
        dishRepository.save(dishMapper.toEntity(dish));
        saveDishIngredients(dish.getId(), dish.getIngredients());
    }

    @Override
    public Optional<Dish> findById(String id) {
        return dishRepository.findById(id)
                .map(entity -> dishMapper.toDomain(entity, findDishIngredients(id)));
    }

    @Override
    public void deleteById(String id) {

    }

    @Override
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

        Map<String, Ingredient> ingredientsById = ingredientRepository.findAllById(ingredientIds).stream()
                .map(ingredientMapper::toDomain)
                .collect(Collectors.toMap(Ingredient::id, Function.identity()));

        return relationEntities.stream()
                .map(entity -> dishIngredientMapper.toDomain(
                        entity,
                        requireIngredient(ingredientsById, entity.getIngredientId())
                ))
                .toList();
    }

    private Ingredient requireIngredient(Map<String, Ingredient> ingredientsById, String ingredientId) {
        Ingredient ingredient = ingredientsById.get(ingredientId);
        if (ingredient == null) {
            throw new IllegalStateException(
                    "Ingredient %s referenced by dish ingredient was not found".formatted(ingredientId)
            );
        }
        return ingredient;
    }

}
