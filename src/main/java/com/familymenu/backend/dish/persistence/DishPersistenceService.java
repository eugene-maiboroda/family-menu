package com.familymenu.backend.dish.persistence;

import com.familymenu.backend.dish.domain.Dish;
import com.familymenu.backend.dish.domain.DishIngredient;
import com.familymenu.backend.dish.domain.ProductReference;
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

    public void saveDish(Dish dish) {
        DishEntity entity = dishMapper.toEntity(dish);
        dishRepository.findById(dish.getId()).
                ifPresent(existing -> entity.setCreatedAt(existing.getCreatedAt()));
        dishRepository.save(entity);
        dishIngredientRepository.deleteAllByDishId(dish.getId());
        dishIngredientRepository.flush();
        saveDishIngredients(dish.getId(), dish.getIngredients());
    }

    public Optional<Dish> findDishById(String id) {
        return dishRepository.findById(id)
                .map(entity -> dishMapper.toDomain(entity, findDishIngredients(id)));
    }

    public void deleteDishById(String id) {
        dishIngredientRepository.deleteAllByDishId(id);
        dishRepository.deleteById(id);
    }

    public List<Dish> findDishes(String searchQuery) {
        String query = "";
        if (searchQuery != null) {
            query = searchQuery.strip();
        }
        return dishRepository.findByNameContainingIgnoreCaseOrderByNameAsc(query).stream()
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
                        entity, new ProductReference(entity.getProductId(), null)
                ))
                .toList();
    }

}
