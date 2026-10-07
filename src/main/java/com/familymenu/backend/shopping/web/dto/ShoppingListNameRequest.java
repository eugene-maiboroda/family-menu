package com.familymenu.backend.shopping.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ShoppingListNameRequest(@NotBlank @Size(max = 255) String name) {}
