package com.method5.jot.scale;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ScaleReader {
    public final ByteArrayInputStream input;

    public ScaleReader(byte[] data) {
        this.input = new ByteArrayInputStream(data);
    }

    public byte readByte() {
        return (byte)input.read();
    }

    public byte[] readBytes(int len) {
        byte[] result = new byte[len];
        try {
            if (input.read(result) != len) throw new IOException("Unexpected EOF");
        } catch (IOException e) {
            throw new RuntimeException("Failed to read " + len + " bytes", e);
        }
        return result;
    }

    public byte[] readCompactBytes() {
        BigInteger len = readCompact();
        return readBytes(len.intValue());
    }

    public BigInteger readCompact() {
        int first = readByte() & 0xFF;
        int mode = first & 0b11;

        if (mode == 0) {
            return BigInteger.valueOf(first >> 2);
        }

        if (mode == 1) {
            int second = readByte() & 0xFF;
            int value = ((first >> 2) | (second << 6));
            return BigInteger.valueOf(value);
        }

        if (mode == 2) {
            int b2 = readByte() & 0xFF;
            int b3 = readByte() & 0xFF;
            int b4 = readByte() & 0xFF;
            long value = ((long)(first >> 2)) |
                    ((long)b2 << 6) |
                    ((long)b3 << 14) |
                    ((long)b4 << 22);
            return BigInteger.valueOf(value);
        }

        int byteLength = (first >> 2) + 4;
        if (byteLength < 4 || byteLength > 67)
            throw new RuntimeException("Invalid compact-encoded integer length: " + byteLength);

        byte[] bytes = readBytes(byteLength);
        return new BigInteger(1, reverse(bytes));
    }

    public int readInt() {
        return (readByte() & 0xFF) |
                ((readByte() & 0xFF) << 8) |
                ((readByte() & 0xFF) << 16) |
                ((readByte() & 0xFF) << 24);
    }

    private byte[] reverse(byte[] arr) {
        for (int i = 0; i < arr.length / 2; i++) {
            byte temp = arr[i];
            arr[i] = arr[arr.length - 1 - i];
            arr[arr.length - 1 - i] = temp;
        }
        return arr;
    }

    public List<String> readStringList() {
        int count = readCompact().intValue();

        List<String> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int strlen = readCompact().intValue();
            byte[] bytes = readBytes(strlen);
            String s = new String(bytes, StandardCharsets.UTF_8);
            list.add(s);
        }
        return list;
    }

    public String readString() {
        byte[] bytes = readCompactBytes();
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public boolean readBoolean() {
        return readByte() != 0;
    }

    public int readU16() {
        return (readByte() & 0xFF) | ((readByte() & 0xFF) << 8);
    }

    public long readU32() {
        return ((long) readByte() & 0xFF) |
                ((long) readByte() & 0xFF) << 8 |
                ((long) readByte() & 0xFF) << 16 |
                ((long) readByte() & 0xFF) << 24;
    }

    public long readU64() {
        long value = 0;
        for (int i = 0; i < 8; i++) {
            value |= ((long) readByte() & 0xFF) << (8 * i);
        }
        return value;
    }

    public BigInteger readU128() {
        byte[] bytes = readBytes(16);
        return new BigInteger(1, reverse(bytes));
    }

    public BigInteger readU256() {
        byte[] bytes = readBytes(32);
        return new BigInteger(1, reverse(bytes));
    }

    public byte readI8() {
        return readByte();
    }

    public short readI16() {
        return (short)readU16();
    }

    public int readI32() {
        return (int)readU32();
    }

    public long readI64() {
        return readU64();
    }

    public BigInteger readI128() {
        byte[] bytes = readBytes(16);
        return new BigInteger(reverse(bytes));
    }

    public BigInteger readI256() {
        byte[] bytes = readBytes(32);
        return new BigInteger(reverse(bytes));
    }

    public char readChar() {
        int codePoint = (int) readU32();
        return (char) codePoint;
    }

    public byte[] readRemaining() {
        return input.readAllBytes();
    }
}
