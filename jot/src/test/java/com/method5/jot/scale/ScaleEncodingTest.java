package com.method5.jot.scale;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScaleEncodingTest {
    @Test
    public void testStringList() {
        List<String> original = List.of("foo", "bar", "baz");

        ScaleWriter writer = new ScaleWriter();
        writer.writeStringList(original);

        ScaleReader reader = new ScaleReader(writer.toByteArray());
        List<String> decoded = reader.readStringList();

        assertEquals(original, decoded);
    }

    @Test
    public void testString() {
        String original = "hello";

        ScaleWriter writer = new ScaleWriter();
        writer.writeString(original);

        ScaleReader reader = new ScaleReader(writer.toByteArray());
        String decoded = reader.readString();

        assertEquals(original, decoded);
    }

    @Test
    public void testBoolean() {
        for (boolean value : new boolean[]{true, false}) {
            ScaleWriter writer = new ScaleWriter();
            writer.writeBoolean(value);

            ScaleReader reader = new ScaleReader(writer.toByteArray());
            boolean decoded = reader.readBoolean();

            assertEquals(value, decoded);
        }
    }

    @Test
    public void testIntegers() {
        int u16 = 65535;
        long u32 = 4294967295L;
        long u64 = 1234567890123456789L;
        BigInteger u128 = new BigInteger("340282366920938463463374607431768211455"); // 2^128 - 1
        BigInteger u256 = new BigInteger("2").pow(256).subtract(BigInteger.ONE);

        ScaleWriter writer = new ScaleWriter();
        writer.writeU16(u16);
        writer.writeU32(u32);
        writer.writeU64(u64);
        writer.writeU128(u128);
        writer.writeU256(u256);

        ScaleReader reader = new ScaleReader(writer.toByteArray());
        assertEquals(u16, reader.readU16());
        assertEquals(u32, reader.readU32());
        assertEquals(u64, reader.readU64());
        assertEquals(u128, reader.readU128());
        assertEquals(u256, reader.readU256());
    }

    @Test
    public void testSignedIntegers() {
        byte i8 = -128;
        short i16 = -32768;
        int i32 = -2147483648;
        long i64 = -9223372036854775808L;
        BigInteger i128 = new BigInteger("-170141183460469231731687303715884105728"); // -2^127
        BigInteger i256 = new BigInteger("-2").pow(255); // -2^255

        ScaleWriter writer = new ScaleWriter();
        writer.writeI8(i8);
        writer.writeI16(i16);
        writer.writeI32(i32);
        writer.writeI64(i64);
        writer.writeI128(i128);
        writer.writeI256(i256);

        ScaleReader reader = new ScaleReader(writer.toByteArray());
        assertEquals(i8, reader.readI8());
        assertEquals(i16, reader.readI16());
        assertEquals(i32, reader.readI32());
        assertEquals(i64, reader.readI64());
        assertEquals(i128, reader.readI128());
        assertEquals(i256, reader.readI256());
    }

    @Test
    public void testChar() {
        char original = 'ł';

        ScaleWriter writer = new ScaleWriter();
        writer.writeChar(original);

        ScaleReader reader = new ScaleReader(writer.toByteArray());
        char decoded = reader.readChar();

        assertEquals(original, decoded);
    }
}
