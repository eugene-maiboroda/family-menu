package com.familymenu.backend.domain.dish;

import lombok.Builder;

@Builder
public record Ingredient(

        String id,

        String name
) {
}
