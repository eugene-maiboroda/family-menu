package com.familymenu.backend.adapter.persistence.mapper;

import com.familymenu.backend.adapter.persistence.entity.DishEntity;
import com.familymenu.backend.domain.dish.Dish;
import com.familymenu.backend.infrastructure.config.MapStructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface DishPersistenceMapper {

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    DishEntity toEntity(Dish dish);

    Dish toDomain(DishEntity entity);
}
