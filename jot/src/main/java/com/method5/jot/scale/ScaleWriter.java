package com.method5.jot.scale;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Function;

/**
 * ScaleWriter — class for scale writer in the Jot SDK. Provides key management and signing; SCALE
 * codec utilities.
 */
public class ScaleWriter {
    private final ByteArrayOutputStream stream = new ByteArrayOutputStream();

    public void writeByte(int b) {
        stream.write(b & 0xFF);
    }

    public void writeBytes(byte[] bytes) {
        try {
            stream.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeCompactBytes(byte[] bytes) {
        writeCompact(BigInteger.valueOf(bytes.length));
        writeBytes(bytes);
    }

    public void writeCompact(BigInteger value) {
        if (value.signum() < 0) {
            throw new IllegalArgumentException("Compact encoding does not support negative values.");
        }

        if (value.compareTo(BigInteger.valueOf(1L << 6)) < 0) {
            // single-byte mode: [00 + value << 2]
            stream.writeBytes(new byte[]{ value.shiftLeft(2).byteValue() });
        } else if (value.compareTo(BigInteger.valueOf(1L << 14)) < 0) {
            // two-byte mode: [01 + value << 2]
            int shifted = value.shiftLeft(2).intValue() | 0x01;
            stream.writeBytes(new byte[] {
                    (byte)(shifted & 0xFF),
                    (byte)((shifted >> 8) & 0xFF)
            });
        } else if (value.compareTo(BigInteger.valueOf(1L << 30)) < 0) {
            // four-byte mode: [10 + value << 2]
            long shifted = value.shiftLeft(2).longValue() | 0x02;
            stream.writeBytes(new byte[] {
                    (byte)(shifted & 0xFF),
                    (byte)((shifted >> 8) & 0xFF),
                    (byte)((shifted >> 16) & 0xFF),
                    (byte)((shifted >> 24) & 0xFF)
            });
        } else {
            // big-integer mode: [11 + length byte + LE bytes]
            byte[] bytes = value.toByteArray();
            byte[] stripped = stripLeadingZeros(bytes);
            reverseInPlace(stripped);
            int length = stripped.length;

            if (length > 67) {
                throw new IllegalArgumentException("Compact encoding does not support values > 2**536 - 1");
            }

            byte[] result = new byte[length + 1];
            result[0] = (byte)((((length - 4) << 2) | 0x03) & 0xFF);
            System.arraycopy(stripped, 0, result, 1, length);
            stream.writeBytes(result);
        }
    }

    public static void reverseInPlace(byte[] arr) {
        for (int i = 0, j = arr.length - 1; i < j; i++, j--) {
            byte temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }

    public void writeInt(int value) {
        stream.write(value & 0xFF);
        stream.write((value >> 8) & 0xFF);
        stream.write((value >> 16) & 0xFF);
        stream.write((value >> 24) & 0xFF);
    }

    public void writeStringList(List<String> list) {
        writeCompact(BigInteger.valueOf(list.size()));
        for (String s : list) {
            byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
            writeCompact(BigInteger.valueOf(bytes.length));
            writeBytes(bytes);
        }
    }

    public void writeString(String s) {
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        writeCompact(BigInteger.valueOf(bytes.length));
        writeBytes(bytes);
    }

    public void writeBoolean(boolean value) {
        writeByte(value ? 1 : 0);
    }

    public void writeU16(int value) {
        writeByte(value & 0xFF);
        writeByte((value >>> 8) & 0xFF);
    }

    public void writeU32(long value) {
        writeByte((int)(value & 0xFF));
        writeByte((int)((value >>> 8) & 0xFF));
        writeByte((int)((value >>> 16) & 0xFF));
        writeByte((int)((value >>> 24) & 0xFF));
    }

    public void writeU64(long value) {
        for (int i = 0; i < 8; i++) {
            writeByte((int)((value >>> (8 * i)) & 0xFF));
        }
    }

    public void writeU128(BigInteger value) {
        if (value.signum() < 0 || value.bitLength() > 128) {
            throw new IllegalArgumentException("U128 must be between 0 and 2^128-1");
        }

        byte[] raw = value.toByteArray();
        byte[] padded = new byte[16];

        int copyLen = Math.min(raw.length, 16);
        for (int i = 0; i < copyLen; i++) {
            padded[i] = raw[raw.length - 1 - i];
        }

        writeBytes(padded);
    }

    public void writeU256(BigInteger value) {
        byte[] bytes = toFixedLengthLE(value, 32);
        writeBytes(bytes);
    }

    public void writeI8(byte value) {
        writeByte(value);
    }

    public void writeI16(short value) {
        writeU16(Short.toUnsignedInt(value));
    }

    public void writeI32(int value) {
        writeU32(Integer.toUnsignedLong(value));
    }

    public void writeI64(long value) {
        writeU64(value);
    }

    public void writeI128(BigInteger value) {
        byte[] bytes = toFixedLengthLE(value, 16);
        writeBytes(bytes);
    }

    public void writeI256(BigInteger value) {
        byte[] bytes = toFixedLengthLE(value, 32);
        writeBytes(bytes);
    }

    public void writeChar(char c) {
        writeU32(c);
    }

    private byte[] toFixedLengthLE(BigInteger value, int size) {
        byte[] le = new byte[size];
        byte[] src = value.toByteArray();
        int copyLen = Math.min(src.length, size);
        for (int i = 0; i < copyLen; i++) {
            le[i] = src[src.length - 1 - i]; // little-endian
        }
        return le;
    }

    private static byte[] stripLeadingZeros(byte[] input) {
        int start = 0;
        while (start < input.length - 1 && input[start] == 0) {
            start++;
        }
        int length = input.length - start;
        byte[] result = new byte[length];
        System.arraycopy(input, start, result, 0, length);
        return result;
    }

    public byte[] toByteArray() {
        return stream.toByteArray();
    }


    public void writeByteArrayVector(List<byte[]> vectors) {
        writeCompact(BigInteger.valueOf(vectors.size()));
        for (byte[] arr : vectors) {
            writeBytes(arr);
        }
    }

    public <T> void writeOptional(T value, Function<T, byte[]> encoder) {
        if (value == null) {
            writeByte(0);
        } else {
            writeByte(1);
            writeBytes(encoder.apply(value));
        }
    }
}
