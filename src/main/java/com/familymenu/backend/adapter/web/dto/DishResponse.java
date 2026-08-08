package com.familymenu.backend.adapter.web.dto;

import com.familymenu.backend.domain.DishCategory;

import java.util.List;

public record DishResponse(
        String id,
        String name,
        String description,
        DishCategory category,
        List<IngredientResponse> ingredients
) {
}