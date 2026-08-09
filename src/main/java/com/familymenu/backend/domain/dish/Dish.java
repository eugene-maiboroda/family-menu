package com.familymenu.backend.domain.dish;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
public class Dish {

    private String id;

    private String name;

    private List<RecipeStep> recipeSteps;

    private DishCategory category;

    private List<DishIngredient> ingredients;

}
