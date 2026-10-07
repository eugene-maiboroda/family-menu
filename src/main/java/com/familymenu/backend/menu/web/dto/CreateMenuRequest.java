package com.familymenu.backend.menu.web.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CreateMenuRequest(

        @NotEmpty
        List<@NotBlank String> dishIds
) {
}
