package com.familymenu.backend.adapter.web.dto;

import com.familymenu.backend.domain.DishCategory;

import java.util.List;

public record CreateDishRequest(
        String name,
        String descriptions,
        DishCategory category,

        List<IngredientRequest> ingredients
) {

}
