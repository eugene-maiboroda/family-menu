package com.familymenu.backend.dish.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DishRepository extends JpaRepository<DishEntity, String> {

    List<DishEntity> findByNameContainingIgnoreCaseOrderByNameAsc(String query);
}
