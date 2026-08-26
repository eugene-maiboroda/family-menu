package com.familymenu.backend.adapter.persistence.mapper;

import com.familymenu.backend.adapter.persistence.entity.DishIngredientEntity;
import com.familymenu.backend.domain.dish.DishIngredient;
import com.familymenu.backend.domain.dish.Ingredient;
import com.familymenu.backend.infrastructure.config.MapStructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface DishIngredientPersistenceMapper {


    @Mapping(target = "dishId", source = "dishId")
    @Mapping(target = "ingredientId", source = "dishIngredient.ingredient.id")
    @Mapping(target = "id", source = "dishIngredient.id")
    @Mapping(target = "amount", source = "dishIngredient.amount")
    @Mapping(target = "unit", source = "dishIngredient.unit")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    DishIngredientEntity toEntity(String dishId, DishIngredient dishIngredient);

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "ingredient", source = "ingredient")
    @Mapping(target = "amount", source = "entity.amount")
    @Mapping(target = "unit", source = "entity.unit")
    DishIngredient toDomain(DishIngredientEntity entity, Ingredient ingredient);
}
