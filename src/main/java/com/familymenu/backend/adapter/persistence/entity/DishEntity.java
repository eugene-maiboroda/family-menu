package com.familymenu.backend.adapter.persistence.entity;

import com.familymenu.backend.domain.dish.DishCategory;
import com.familymenu.backend.domain.dish.RecipeStep;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "dishes")
public class DishEntity {

    @Id
    private String id;

    private String name;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "description", columnDefinition = "jsonb")
    private List<RecipeStep> recipeSteps;

    @Enumerated(EnumType.STRING)
    private DishCategory category;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}