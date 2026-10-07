package com.familymenu.backend.dish.web.dto;

import jakarta.validation.constraints.NotBlank;

public record RecipeStepRequest(

        @NotBlank String description,

        String imageUrl
) {
}
