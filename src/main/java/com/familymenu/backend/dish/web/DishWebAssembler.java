package com.familymenu.backend.dish.web;

import com.familymenu.backend.dish.domain.Dish;
import com.familymenu.backend.dish.domain.DishCategory;
import com.familymenu.backend.dish.domain.DishIngredient;
import com.familymenu.backend.dish.domain.IngredientReference;
import com.familymenu.backend.dish.domain.RecipeStep;
import com.familymenu.backend.dish.domain.Unit;
import com.familymenu.backend.dish.web.dto.CreateDishRequest;
import com.familymenu.backend.dish.web.dto.DishIngredientResponse;
import com.familymenu.backend.dish.web.dto.DishResponse;
import com.familymenu.backend.dish.web.dto.IngredientRequest;
import com.familymenu.backend.dish.web.dto.RecipeStepRequest;
import com.familymenu.backend.dish.web.dto.RecipeStepResponse;
import com.familymenu.backend.dish.web.dto.UpdateDishRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DishWebAssembler {

    public Dish toDish(CreateDishRequest request) {
        if (request == null) {
            return null;
        }

        return Dish.builder()
                .name(request.name())
                .category(toDishCategory(request.category()))
                .ingredients(toDishIngredients(request.ingredients()))
                .recipeSteps(toRecipeSteps(request.recipeSteps()))
                .build();
    }

    public Dish toDish(UpdateDishRequest request, String id) {
        if (request == null) {
            return null;
        }

        return Dish.builder()
                .id(id)
                .name(request.name())
                .category(toDishCategory(request.category()))
                .ingredients(toDishIngredients(request.ingredients()))
                .recipeSteps(toRecipeSteps(request.recipeSteps()))
                .build();
    }

    public DishResponse toDishResponse(Dish dish) {
        if (dish == null) {
            return null;
        }

        return new DishResponse(
                dish.getId(),
                dish.getName(),
                dish.getCategory() == null ? null : dish.getCategory().name(),
                toDishIngredientResponses(dish.getIngredients()),
                toRecipeStepResponses(dish.getRecipeSteps())
        );
    }

    private DishCategory toDishCategory(String category) {
        return category == null ? null : DishCategory.valueOf(category);
    }

    private List<DishIngredient> toDishIngredients(List<IngredientRequest> requests) {
        if (requests == null) {
            return null;
        }
        return requests.stream()
                .map(this::toDishIngredient)
                .toList();
    }

    private DishIngredient toDishIngredient(IngredientRequest request) {
        if (request == null) {
            return null;
        }
        return new DishIngredient(
                null,
                new IngredientReference(null, request.name()),
                request.amount(),
                Unit.valueOf(request.unit())
        );
    }

    private List<RecipeStep> toRecipeSteps(List<RecipeStepRequest> requests) {
        if (requests == null) {
            return null;
        }
        return requests.stream()
                .map(request -> new RecipeStep(request.description(), request.imageUrl()))
                .toList();
    }

    private List<DishIngredientResponse> toDishIngredientResponses(List<DishIngredient> ingredients) {
        if (ingredients == null) {
            return null;
        }
        return ingredients.stream()
                .map(ingredient -> new DishIngredientResponse(
                        ingredient.ingredient().id(),
                        ingredient.ingredient().name(),
                        ingredient.amount(),
                        ingredient.unit().name()
                ))
                .toList();
    }

    private List<RecipeStepResponse> toRecipeStepResponses(List<RecipeStep> steps) {
        if (steps == null) {
            return null;
        }
        return steps.stream()
                .map(step -> new RecipeStepResponse(step.description(), step.imageUrl()))
                .toList();
    }
}
