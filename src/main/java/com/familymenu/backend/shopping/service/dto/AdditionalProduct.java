package com.familymenu.backend.shopping.service.dto;

import java.math.BigDecimal;

public record AdditionalProduct(

        String productId,

        BigDecimal amount,

        String unit
) {
}
