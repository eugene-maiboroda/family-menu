package com.familymenu.backend.domain.menu;

import lombok.Builder;

import java.time.Instant;

@Builder
public record Menu(

        String id,

        Instant createdDate
) {

}
