package com.familymenu.backend.dish.persistence;

import com.familymenu.backend.dish.domain.DishCategory;
import com.familymenu.backend.dish.domain.RecipeStep;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "dishes")
public class DishEntity {

    @Id
    private String id;

    private String name;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "recipe_steps", columnDefinition = "jsonb")
    private List<RecipeStep> recipeSteps;

    @Enumerated(EnumType.STRING)
    private DishCategory category;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}
