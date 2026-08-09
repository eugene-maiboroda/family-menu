package com.familymenu.backend.adapter.web.mapper;

import com.familymenu.backend.adapter.web.dto.ShoppingListItemResponse;
import com.familymenu.backend.domain.shopping.ShoppingListItem;
import com.familymenu.backend.infrastructure.MapStructConfig;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = MapStructConfig.class)
public interface ShoppingListMapper {

    List<ShoppingListItemResponse> toResponse(List<ShoppingListItem> items);

}
