package com.familymenu.backend.shopping.web;

import com.familymenu.backend.shopping.domain.ShoppingListItem;
import com.familymenu.backend.shopping.web.dto.ShoppingListItemResponse;
import com.familymenu.backend.infrastructure.config.MapStructConfig;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = MapStructConfig.class)
public interface ShoppingListWebMapper {

    List<ShoppingListItemResponse> toResponse(List<ShoppingListItem> items);

}
