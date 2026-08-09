package com.familymenu.backend.adapter.web.dto;

import com.familymenu.backend.domain.dish.DishCategory;
import com.familymenu.backend.domain.dish.DishIngredient;
import com.familymenu.backend.domain.dish.RecipeStep;

import java.util.List;

public record DishResponse(

        String id,

        String name,

        DishCategory category,

        List<DishIngredient> ingredients,

        List<RecipeStep> recipeSteps
) {
}