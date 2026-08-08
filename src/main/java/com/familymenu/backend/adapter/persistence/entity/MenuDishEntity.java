package com.familymenu.backend.adapter.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.Instant;

@Entity
@Table(name = "menu_dishes",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"menuId", "dishId"})
        })
public class MenuDishEntity {

    @Id
    private String id;

    private String menuId;

    private String dishId;

    private Instant createdAt;
}
