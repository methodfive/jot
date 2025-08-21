package com.method5.jot.scale;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class CompactEncodingTest {
    @Test
    void testCompactRoundTrip_standardCases() {
        BigInteger[] testValues = {
                BigInteger.ZERO,
                BigInteger.ONE,
                BigInteger.valueOf(63),       // max 1-byte
                BigInteger.valueOf(64),       // min 2-byte
                BigInteger.valueOf(16383),    // max 2-byte
                BigInteger.valueOf(16384),    // min 4-byte
                BigInteger.valueOf(0x3FFFFFFFL)  // max 4-byte
        };

        for (BigInteger value : testValues) {
            ScaleWriter writer = new ScaleWriter();
            writer.writeCompact(value);

            ScaleReader reader = new ScaleReader(writer.toByteArray());
            BigInteger decoded = reader.readCompact();

            assertEquals(value, decoded);
        }
    }

    @Test
    void testCompactRoundTrip_bigIntegerMode() {
        BigInteger[] bigs = {
                new BigInteger("40000000"),                    // 2^30
                new BigInteger("FFFFFFFFFFFFFFFF", 16),        // 2^64 - 1
                new BigInteger("123456789ABCDEF123456789", 16) // very large
        };

        for (BigInteger value : bigs) {
            ScaleWriter writer = new ScaleWriter();
            writer.writeCompact(value);

            ScaleReader reader = new ScaleReader(writer.toByteArray());
            BigInteger decoded = reader.readCompact();

            assertEquals(value, decoded);
        }
    }

    @Test
    void testWriteCompactRejectsNegative() {
        ScaleWriter writer = new ScaleWriter();
        assertThrows(IllegalArgumentException.class, () ->
                writer.writeCompact(BigInteger.valueOf(-1)));
    }

    @Test
    void test1ByteEncoding() {
        BigInteger value = BigInteger.valueOf(42);
        ScaleWriter writer = new ScaleWriter();
        writer.writeCompact(value);
        byte[] bytes = writer.toByteArray();

        assertEquals(1, bytes.length);
        assertEquals(42 << 2, bytes[0] & 0xFF);
    }

    @Test
    void test2ByteEncoding() {
        BigInteger value = BigInteger.valueOf(0b11111111111111); // 16383
        ScaleWriter writer = new ScaleWriter();
        writer.writeCompact(value);
        byte[] bytes = writer.toByteArray();

        assertEquals(2, bytes.length);
        int actual = ((bytes[1] & 0xFF) << 8) | (bytes[0] & 0xFF);
        int expected = (value.intValue() << 2) | 0b01;
        assertEquals(expected, actual);
    }

    @Test
    void test4ByteEncoding() {
        BigInteger value = BigInteger.valueOf(0x3FFF_FFFFL);
        ScaleWriter writer = new ScaleWriter();
        writer.writeCompact(value);
        byte[] bytes = writer.toByteArray();

        assertEquals(4, bytes.length);
        int actual = ((bytes[3] & 0xFF) << 24) |
                ((bytes[2] & 0xFF) << 16) |
                ((bytes[1] & 0xFF) << 8) |
                (bytes[0] & 0xFF);
        int expected = (value.intValue() << 2) | 0b10;
        assertEquals(expected, actual);
    }

    @Test
    void testBigIntegerEncodingByteCount() {
        BigInteger big = new BigInteger("123456789ABCDEF123456789", 16);
        ScaleWriter writer = new ScaleWriter();
        writer.writeCompact(big);
        byte[] encoded = writer.toByteArray();

        // First byte: mode = 0b11 (large integer)
        assertEquals(0b11, encoded[0] & 0b11);

        int lenByte = (encoded[0] & 0xFC) >> 2;
        int valueBytes = lenByte + 4;
        assertEquals(encoded.length - 1, valueBytes);
    }
}
