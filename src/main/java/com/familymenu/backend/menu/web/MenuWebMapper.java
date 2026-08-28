package com.familymenu.backend.menu.web;

import com.familymenu.backend.menu.service.CreateMenuCommand;
import com.familymenu.backend.menu.web.dto.CreateMenuRequest;
import com.familymenu.backend.infrastructure.config.MapStructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public interface MenuWebMapper {

    CreateMenuCommand toDto(CreateMenuRequest request);

}
