package com.familymenu.backend.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class Dish {

    private String id;
    private String name;
    private String description;
    private DishCategory category;

    private List<DishIngredient> ingredients;
}
