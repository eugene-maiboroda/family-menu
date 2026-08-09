package com.familymenu.backend.domain.menu;

import lombok.Builder;

@Builder
public record MenuDish(

        String menuId,

        String dishId
) {

}
