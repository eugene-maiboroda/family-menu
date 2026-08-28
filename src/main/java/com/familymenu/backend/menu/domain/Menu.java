package com.familymenu.backend.menu.domain;

import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record Menu(

        String id,

        Instant createdDate,

        List<String> dishIds
) {

}
