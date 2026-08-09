package com.familymenu.backend.domain.dish;

import lombok.Builder;

@Builder
public record RecipeStep(

        String step,

        String imageUrl
) {

}
