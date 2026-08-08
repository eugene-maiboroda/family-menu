package com.familymenu.backend.adapter.web.dto;

import com.familymenu.backend.domain.DishCategory;

import java.util.List;

public record UpdateDishRequest(
        String descriptions,
        DishCategory category,

        List<IngredientRequest> ingredients
) {
}
