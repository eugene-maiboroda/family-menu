package com.familymenu.backend.domain.dish;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record RecipeStep(

        @NotBlank String description,

        String imageUrl
) {

}
