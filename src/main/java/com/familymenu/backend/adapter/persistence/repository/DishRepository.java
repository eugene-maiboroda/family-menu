package com.familymenu.backend.adapter.persistence.repository;

import com.familymenu.backend.adapter.persistence.entity.DishEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishRepository extends JpaRepository<DishEntity, String> {
}
