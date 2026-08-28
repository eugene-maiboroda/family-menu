package com.familymenu.backend.dish.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DishIngredientRepository  extends JpaRepository<DishIngredientEntity, String> {

    List<DishIngredientEntity> findAllByDishId(String dishId);
}
