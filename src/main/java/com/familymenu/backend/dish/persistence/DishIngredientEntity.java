package com.familymenu.backend.dish.persistence;

import com.familymenu.backend.dish.domain.Unit;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
@Getter
@Setter
@NoArgsConstructor
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
