package com.familymenu.backend.dish.web.dto;

import java.util.List;

public record DishResponse(

        String id,

        String name,

        String category,

        List<DishIngredientResponse> ingredients,

        List<RecipeStepResponse> recipeSteps
) {
}
