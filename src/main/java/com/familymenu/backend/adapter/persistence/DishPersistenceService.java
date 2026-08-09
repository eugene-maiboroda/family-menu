package com.familymenu.backend.adapter.persistence;

import com.familymenu.backend.adapter.persistence.entity.DishIngredientEntity;
import com.familymenu.backend.adapter.persistence.mapper.DishIngredientPersistenceMapper;
import com.familymenu.backend.adapter.persistence.mapper.DishPersistenceMapper;
import com.familymenu.backend.adapter.persistence.repository.DishIngredientRepository;
import com.familymenu.backend.adapter.persistence.repository.DishRepository;
import com.familymenu.backend.domain.dish.Dish;
import com.familymenu.backend.domain.dish.DishIngredient;
import com.familymenu.backend.port.out.DishPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DishPersistenceService implements DishPersistencePort {

    private final DishRepository dishRepository;
    private final DishPersistenceMapper dishMapper;

    private final DishIngredientRepository dishIngredientRepository;
    private final DishIngredientPersistenceMapper dishIngredientMapper;

    @Override
    public void save(Dish dish) {
        dishRepository.save(dishMapper.toEntity(dish));
    }

    @Override
    public Optional<Dish> findById(String id) {
        return dishRepository.findById(id)
                .map(dishMapper::toDomain);
    }

    @Override
    public void deleteById(String id) {

    }

    @Override
    public List<Dish> findAll() {
        return List.of();
    }

    @Override
    public void saveDishIngredients(String dishId, List<DishIngredient> ingredients) {
        List<DishIngredientEntity> entities = ingredients.stream()
                .map(ingredient -> dishIngredientMapper.toEntity(dishId, ingredient))
                .toList();

        dishIngredientRepository.saveAll(entities);
    }

}
