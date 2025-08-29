package com.method5.jot.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * UnitConverter — class for unit converter in the Jot SDK. Provides utility helpers.
 */
public final class UnitConverter {
    private UnitConverter() {}

    private static final int DOT_DECIMALS = 10;

    public static BigInteger toPlanck(BigDecimal dotAmount) {
        return dotAmount.multiply(BigDecimal.TEN.pow(DOT_DECIMALS)).toBigIntegerExact();
    }

    public static BigDecimal fromPlanck(BigInteger planckAmount, int decimals) {
        return new BigDecimal(planckAmount).divide(BigDecimal.TEN.pow(decimals), RoundingMode.DOWN);
    }
}
