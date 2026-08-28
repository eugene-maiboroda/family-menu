package com.familymenu.backend.ingredient.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IngredientRepository extends JpaRepository<IngredientEntity, String> {

    Optional<IngredientEntity> findByName(String name);
}
