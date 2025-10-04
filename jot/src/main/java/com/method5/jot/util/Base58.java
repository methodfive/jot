package com.method5.jot.util;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Base58 — class for base 58 in the Jot SDK. Provides utility helpers.
 */
public final class Base58 {
    private Base58() {}

    private static final char[] ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".toCharArray();
    private static final int[] INDEXES = new int[128];

    static {
        Arrays.fill(INDEXES, -1);
        for (int i = 0; i < ALPHABET.length; i++) {
            INDEXES[ALPHABET[i]] = i;
        }
    }

    public static String encode(byte[] input) {
        if (input.length == 0) return "";

        input = Arrays.copyOf(input, input.length); // don't mutate

        int zeroCount = 0;
        while (zeroCount < input.length && input[zeroCount] == 0) {
            ++zeroCount;
        }

        byte[] temp = new byte[input.length * 2];
        int j = temp.length;

        int startAt = zeroCount;
        while (startAt < input.length) {
            int mod = divmod58(input, startAt);
            if (input[startAt] == 0) {
                ++startAt;
            }
            temp[--j] = (byte) ALPHABET[mod];
        }

        while (j < temp.length && temp[j] == ALPHABET[0]) {
            ++j;
        }

        while (--zeroCount >= 0) {
            temp[--j] = (byte) ALPHABET[0];
        }

        return new String(Arrays.copyOfRange(temp, j, temp.length), StandardCharsets.US_ASCII);
    }

    public static byte[] decode(String input) {
        if (input.isEmpty()) return new byte[0];

        byte[] input58 = new byte[input.length()];
        for (int i = 0; i < input.length(); ++i) {
            char c = input.charAt(i);
            int digit = c < 128 ? INDEXES[c] : -1;
            if (digit < 0) throw new IllegalArgumentException("Invalid Base58 character: " + c);
            input58[i] = (byte) digit;
        }

        int zeroCount = 0;
        while (zeroCount < input58.length && input58[zeroCount] == 0) {
            ++zeroCount;
        }

        byte[] temp = new byte[input.length()];
        int j = temp.length;

        int startAt = zeroCount;
        while (startAt < input58.length) {
            int mod = divmod256(input58, startAt);
            if (input58[startAt] == 0) {
                ++startAt;
            }
            temp[--j] = (byte) mod;
        }

        while (j < temp.length && temp[j] == 0) {
            ++j;
        }

        return Arrays.copyOfRange(temp, j - zeroCount, temp.length);
    }

    private static int divmod58(byte[] number, int startAt) {
        int remainder = 0;
        for (int i = startAt; i < number.length; i++) {
            int digit = number[i] & 0xFF;
            int temp = remainder * 256 + digit;
            number[i] = (byte) (temp / 58);
            remainder = temp % 58;
        }
        return remainder;
    }

    private static int divmod256(byte[] number58, int startAt) {
        int remainder = 0;
        for (int i = startAt; i < number58.length; i++) {
            int digit = number58[i] & 0xFF;
            int temp = remainder * 58 + digit;
            number58[i] = (byte) (temp / 256);
            remainder = temp % 256;
        }
        return remainder;
    }
}
