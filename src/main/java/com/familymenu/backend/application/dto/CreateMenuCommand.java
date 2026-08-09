package com.familymenu.backend.application.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record CreateMenuCommand(

        List<String> dishIds
) {
}
