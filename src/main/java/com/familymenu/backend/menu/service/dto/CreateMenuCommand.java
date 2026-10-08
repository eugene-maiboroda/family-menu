package com.familymenu.backend.menu.service.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record CreateMenuCommand(

        String name,

        List<String> dishIds
) {
}
