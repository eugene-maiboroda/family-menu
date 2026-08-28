package com.familymenu.backend.ingredient.persistence;

import com.familymenu.backend.ingredient.api.dto.IngredientView;
import com.familymenu.backend.ingredient.domain.Ingredient;
import com.familymenu.backend.infrastructure.config.MapStructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface IngredientMapper {

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    IngredientEntity toEntity(Ingredient ingredient);

    Ingredient toDomain(IngredientEntity entity);

    IngredientView toView(Ingredient ingredient);

    default IngredientView toView(IngredientEntity entity) {
        return toView(toDomain(entity));
    }
}
