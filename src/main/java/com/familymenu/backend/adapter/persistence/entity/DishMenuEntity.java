package com.familymenu.backend.adapter.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "dish_menu")
public class DishMenuEntity {

    @Id
    private String id;

    private String dishId;

    private String menuId;
}
