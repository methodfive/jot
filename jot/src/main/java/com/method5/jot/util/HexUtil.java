package com.method5.jot.util;

/**
 * HexUtil — class for hex util in the Jot SDK. Provides utility helpers.
 */
public class HexUtil {
    public static String trim(String hex) {
        if (hex.startsWith("0x") || hex.startsWith("0X")) {
            return hex.substring(2);
        }
        return hex;
    }
    public static byte[] hexToBytes(String hex) {
        if (hex == null || hex.isEmpty()) {
            return null;
        }

        if (hex.startsWith("0x") || hex.startsWith("0X")) {
            hex = hex.substring(2);
        }

        if (hex.length() % 2 != 0) {
            throw new IllegalArgumentException("Hex string must have even length");
        }

        int len = hex.length();
        byte[] result = new byte[len / 2];

        for (int i = 0; i < len; i += 2) {
            int byteValue = Integer.parseInt(hex.substring(i, i + 2), 16);
            result[i / 2] = (byte) byteValue;
        }

        return result;
    }

    public static String bytesToHex(byte[] all) {
        StringBuilder sb = new StringBuilder(all.length * 2);
        for (byte b : all) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
