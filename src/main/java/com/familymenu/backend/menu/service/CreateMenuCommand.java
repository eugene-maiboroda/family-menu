package com.familymenu.backend.menu.service;

import lombok.Builder;

import java.util.List;

@Builder
public record CreateMenuCommand(

        List<String> dishIds
) {
}
