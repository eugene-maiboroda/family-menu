package com.familymenu.backend.dish.persistence;

import com.familymenu.backend.dish.domain.Dish;
import com.familymenu.backend.dish.domain.DishIngredient;
import com.familymenu.backend.dish.domain.IngredientReference;
import com.familymenu.backend.dish.persistence.mapper.DishIngredientPersistenceMapper;
import com.familymenu.backend.dish.persistence.mapper.DishPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DishPersistenceService {

    private final DishRepository dishRepository;
    private final DishPersistenceMapper dishMapper;

    private final DishIngredientRepository dishIngredientRepository;
    private final DishIngredientPersistenceMapper dishIngredientMapper;

    public void save(Dish dish) {
        dishRepository.save(dishMapper.toEntity(dish));
        saveDishIngredients(dish.getId(), dish.getIngredients());
    }

    public Optional<Dish> findById(String id) {
        return dishRepository.findById(id)
                .map(entity -> dishMapper.toDomain(entity, findDishIngredients(id)));
    }

    public void deleteById(String id) {
        dishIngredientRepository.deleteAllByDishId(id);
        dishRepository.deleteById(id);
    }

    public List<Dish> findAll() {
        return dishRepository.findAll().stream()
                .map(entity -> dishMapper.toDomain(entity, findDishIngredients(entity.getId())))
                .toList();
    }

    private void saveDishIngredients(String dishId, List<DishIngredient> ingredients) {
        List<DishIngredientEntity> entities = ingredients.stream()
                .map(ingredient -> dishIngredientMapper.toEntity(dishId, ingredient))
                .toList();

        dishIngredientRepository.saveAll(entities);
    }

    private List<DishIngredient> findDishIngredients(String dishId) {
        return dishIngredientRepository.findAllByDishId(dishId).stream()
                .map(entity -> dishIngredientMapper.toDomain(
                        entity,
                        new IngredientReference(entity.getIngredientId(), null)
                ))
                .toList();
    }

}
