package com.familymenu.backend.adapter.persistence.mapper;

import com.familymenu.backend.adapter.persistence.entity.IngredientEntity;
import com.familymenu.backend.domain.dish.Ingredient;
import com.familymenu.backend.infrastructure.config.MapStructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface IngredientPersistenceMapper {

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    IngredientEntity toEntity(Ingredient ingredient);

  Ingredient toDomain(IngredientEntity entity);
}
