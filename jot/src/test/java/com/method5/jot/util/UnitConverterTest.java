package com.method5.jot.util;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class UnitConverterTest {
    @Test
    void testToPlanckWithWholeDOT() {
        BigDecimal dot = new BigDecimal("1");
        BigInteger expectedPlanck = new BigInteger("10000000000"); // 1 DOT = 10^10 Planck
        assertEquals(expectedPlanck, UnitConverter.toPlanck(dot));
    }

    @Test
    void testToPlanckWithFractionalDOT() {
        BigDecimal dot = new BigDecimal("0.000000001"); // 1 nanoDOT
        BigInteger expected = new BigInteger("10");
        assertEquals(expected, UnitConverter.toPlanck(dot));
    }

    @Test
    void testToPlanckWithRoundingErrorFails() {
        BigDecimal nonExact = new BigDecimal("0.00000000001"); // not exactly divisible into Planck
        assertThrows(ArithmeticException.class, () -> UnitConverter.toPlanck(nonExact));
    }

    @Test
    void testFromPlanckWithDefaultDOTDecimals() {
        BigInteger planck = new BigInteger("10000000000");
        BigDecimal dot = UnitConverter.fromPlanck(planck, 10);
        assertEquals(new BigDecimal("1.0").intValue(), dot.intValue());
    }

    @Test
    void testFromPlanckWithCustomDecimals() {
        BigInteger value = new BigInteger("100000");
        BigDecimal result = UnitConverter.fromPlanck(value, 5);
        assertEquals(0, result.compareTo(new BigDecimal("1.0")));
    }
}