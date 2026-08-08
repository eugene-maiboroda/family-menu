package com.familymenu.backend.adapter.persistence.entity;

import com.familymenu.backend.domain.Unit;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "dish_ingredients")
public class DishIngredientEntity {

    @Id
    private String id;

    private String dishId;

    private String ingredientId;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private Unit unit;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}