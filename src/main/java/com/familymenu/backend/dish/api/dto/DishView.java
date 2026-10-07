package com.familymenu.backend.dish.api.dto;

import java.util.List;

public record DishView(

        String id,

        String name,

        String category,

        List<DishIngredientView> ingredients,

        List<RecipeStepView> recipeSteps
) {
}
