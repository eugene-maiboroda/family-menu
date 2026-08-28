package com.familymenu.backend.shopping.domain;

import java.math.BigDecimal;

public enum Unit {
    G(Dimension.MASS, BigDecimal.ONE),
    KG(Dimension.MASS, new BigDecimal("1000")),
    ML(Dimension.VOLUME, BigDecimal.ONE),
    L(Dimension.VOLUME, new BigDecimal("1000")),
    PCS(Dimension.COUNT, BigDecimal.ONE);

    private final Dimension dimension;
    private final BigDecimal baseMultiplier;

    Unit(Dimension dimension, BigDecimal baseMultiplier) {
        this.dimension = dimension;
        this.baseMultiplier = baseMultiplier;
    }

    public Dimension dimension() {
        return dimension;
    }

    public BigDecimal toBase(BigDecimal amount) {
        return amount.multiply(baseMultiplier);
    }

    public static Unit baseUnit(Dimension dimension) {
        return switch (dimension) {
            case MASS -> G;
            case VOLUME -> ML;
            case COUNT -> PCS;
        };
    }

    public static Unit displayUnit(Dimension dimension, BigDecimal baseAmount) {
        if (baseAmount.compareTo(new BigDecimal("1000")) >= 0) {
            return switch (dimension) {
                case MASS -> KG;
                case VOLUME -> L;
                case COUNT -> PCS;
            };
        }
        return baseUnit(dimension);
    }

    public BigDecimal fromBase(BigDecimal baseAmount) {
        BigDecimal normalized = baseAmount.divide(baseMultiplier).stripTrailingZeros();
        return new BigDecimal(normalized.toPlainString());
    }

    public enum Dimension {
        MASS,
        VOLUME,
        COUNT
    }
}
