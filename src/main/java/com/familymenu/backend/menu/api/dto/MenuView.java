package com.familymenu.backend.menu.api.dto;

import java.time.Instant;
import java.util.List;

public record MenuView(
        String id,

        String name,
        Instant createdDate,
        List<String> dishIds
) {
}
