package com.familymenu.backend.adapter.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "menu_dishes",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"menu_id", "dish_id"})
        })
public class MenuDishEntity {

    @Id
    private String id;

    @Column(name = "menu_id", nullable = false)
    private String menuId;

    @Column(name = "dish_id")
    private String dishId;

    @CreationTimestamp
    private Instant createdAt;
}
