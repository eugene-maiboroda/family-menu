package com.familymenu.backend.adapter.web.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateMenuRequest(

        @NotEmpty
        List<String> dishIds
) {
}