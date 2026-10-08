package com.familymenu.backend.menu.web.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateMenuRequest(

        @NotBlank
        @Size(max = 255)
        String name,

        @NotEmpty
        List<@NotBlank String> dishIds
) {
}
