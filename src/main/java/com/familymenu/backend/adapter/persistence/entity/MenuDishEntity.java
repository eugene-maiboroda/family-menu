package com.familymenu.backend.adapter.persistence.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "menu_dishes",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"menu_id", "dish_id"})
        })
public class MenuDishEntity {

    @Id
    private String id;

    @Column(name = "menu_id")
    private String menuId;

    @Column(name = "dish_id")
    private String dishId;

    private Instant createdAt;
}
