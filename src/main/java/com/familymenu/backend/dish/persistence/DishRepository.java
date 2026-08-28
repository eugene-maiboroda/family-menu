package com.familymenu.backend.dish.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DishRepository extends JpaRepository<DishEntity, String> {
}
