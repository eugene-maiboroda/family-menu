package com.familymenu.backend.adapter.web.dto;

import java.util.List;

public record CreateMenuRequest(
        List<String> dishIds
) {
}