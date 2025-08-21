package com.method5.jot.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HexUtilTest {
    @Test
    void testHexToBytesWithPrefix() {
        byte[] result = HexUtil.hexToBytes("0xdeadbeef");
        assertArrayEquals(new byte[]{(byte) 0xde, (byte) 0xad, (byte) 0xbe, (byte) 0xef}, result);
    }

    @Test
    void testHexToBytesWithUppercasePrefix() {
        byte[] result = HexUtil.hexToBytes("0XCAFE");
        assertArrayEquals(new byte[]{(byte) 0xca, (byte) 0xfe}, result);
    }

    @Test
    void testHexToBytesWithoutPrefix() {
        byte[] result = HexUtil.hexToBytes("0102");
        assertArrayEquals(new byte[]{1, 2}, result);
    }

    @Test
    void testHexToBytesNullAndEmpty() {
        assertNull(HexUtil.hexToBytes(null));
        assertNull(HexUtil.hexToBytes(""));
    }

    @Test
    void testHexToBytesThrowsOnOddLength() {
        assertThrows(IllegalArgumentException.class, () -> HexUtil.hexToBytes("abc"));
    }

    @Test
    void testBytesToHexLowercaseOutput() {
        byte[] input = new byte[]{(byte) 0xde, (byte) 0xad, (byte) 0xbe, (byte) 0xef};
        String hex = HexUtil.bytesToHex(input);
        assertEquals("deadbeef", hex);
    }

    @Test
    void testRoundTrip() {
        String original = "0x1234567890abcdef";
        byte[] bytes = HexUtil.hexToBytes(original);
        String roundTrip = HexUtil.bytesToHex(bytes);
        assertEquals("1234567890abcdef", roundTrip);
    }
}
