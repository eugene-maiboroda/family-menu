package com.familymenu.backend.adapter.persistence.repository;

import com.familymenu.backend.adapter.persistence.entity.IngredientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends JpaRepository<IngredientEntity, String> {
}
