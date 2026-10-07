package com.familymenu.backend.dish.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateDishRequest(

        @NotBlank
        String name,

        @NotNull
        String category,

        @NotEmpty
        List<@NotNull @Valid IngredientRequest> ingredients,

        List<@NotNull @Valid RecipeStepRequest> recipeSteps
) {

}
