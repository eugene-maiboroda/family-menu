package com.familymenu.backend.adapter.web.mapper;

import com.familymenu.backend.adapter.web.dto.CreateMenuRequest;
import com.familymenu.backend.application.dto.CreateMenuCommand;
import com.familymenu.backend.infrastructure.config.MapStructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public interface MenuMapper {

    CreateMenuCommand toDto(CreateMenuRequest request);

}
