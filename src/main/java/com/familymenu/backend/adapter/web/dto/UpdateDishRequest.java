package com.familymenu.backend.adapter.web.dto;

import com.familymenu.backend.domain.dish.DishCategory;
import jakarta.validation.Valid;

import java.util.List;

public record UpdateDishRequest(

        String descriptions,

        DishCategory category,

        @Valid
        List<IngredientRequest> ingredients
) {
}
