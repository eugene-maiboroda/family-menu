package com.familymenu.backend.dish.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record RecipeStep(

        @NotBlank String description,

        String imageUrl
) {

}
