package com.familymenu.backend.adapter.web.mapper;

import com.familymenu.backend.adapter.web.dto.CreateDishRequest;
import com.familymenu.backend.adapter.web.dto.DishResponse;
import com.familymenu.backend.adapter.web.dto.UpdateDishRequest;
import com.familymenu.backend.domain.dish.Dish;
import com.familymenu.backend.infrastructure.MapStructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public interface DishMapper {

    Dish toDish(CreateDishRequest request);

    Dish toDish(UpdateDishRequest request);

    DishResponse toDishResponse(Dish dish);
}
