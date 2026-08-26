package com.familymenu.backend.adapter.persistence.repository;

import com.familymenu.backend.adapter.persistence.entity.DishIngredientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishIngredientRepository  extends JpaRepository<DishIngredientEntity, String> {

    List<DishIngredientEntity> findAllByDishId(String dishId);
}
