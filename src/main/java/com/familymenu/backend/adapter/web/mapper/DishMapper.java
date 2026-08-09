package com.familymenu.backend.adapter.web.mapper;

import com.familymenu.backend.adapter.web.dto.CreateDishRequest;
import com.familymenu.backend.adapter.web.dto.DishResponse;
import com.familymenu.backend.adapter.web.dto.IngredientRequest;
import com.familymenu.backend.adapter.web.dto.UpdateDishRequest;
import com.familymenu.backend.domain.dish.Dish;
import com.familymenu.backend.domain.dish.DishIngredient;
import com.familymenu.backend.domain.dish.Ingredient;
import com.familymenu.backend.infrastructure.config.MapStructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface DishMapper {

    @Mapping(target = "id", ignore = true)
    Dish toDish(CreateDishRequest request);

    @Mapping(target = "id", source = "id")
    Dish toDish(UpdateDishRequest request, String id);

    DishResponse toDishResponse(Dish dish);

    default DishIngredient toDishIngredient(
            IngredientRequest request
    ) {
        if (request == null) {
            return null;
        }

        return new DishIngredient(
                null,
                new Ingredient(null, request.name()),
                request.amount(),
                request.unit()
        );
    }
}