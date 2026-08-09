package com.familymenu.backend.port.out;

import com.familymenu.backend.domain.dish.Dish;
import com.familymenu.backend.domain.dish.DishIngredient;

import java.util.List;
import java.util.Optional;

public interface DishPersistencePort {

    void save(Dish dish);

    Optional<Dish> findById(String id);

    void deleteById(String id);

    List<Dish> findAll();

    void saveDishIngredients(String dishId, List<DishIngredient> ingredients);
}
