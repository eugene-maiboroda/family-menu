package com.familymenu.backend.adapter.web.dto;

import com.familymenu.backend.domain.dish.DishCategory;
import com.familymenu.backend.domain.dish.RecipeStep;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateDishRequest(

        @NotBlank
        String name,

        @NotNull
        DishCategory category,

        @NotEmpty
        @Valid
        List<IngredientRequest> ingredients,

        List<RecipeStep> recipeSteps
) {

}
