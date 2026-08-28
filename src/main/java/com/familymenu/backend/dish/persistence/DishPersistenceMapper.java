package com.familymenu.backend.dish.persistence;

import com.familymenu.backend.dish.domain.Dish;
import com.familymenu.backend.dish.domain.DishIngredient;
import com.familymenu.backend.infrastructure.config.MapStructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = MapStructConfig.class)
public interface DishPersistenceMapper {

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    DishEntity toEntity(Dish dish);

    @Mapping(target = "ingredients", source = "ingredients")
    Dish toDomain(DishEntity entity, List<DishIngredient> ingredients);
}
