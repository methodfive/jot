package com.method5.jot.wallet;

import com.method5.jot.crypto.Hasher;
import com.method5.jot.util.Base58;

import java.util.Arrays;

/**
 * SS58 — class for ss 58 in the Jot SDK. Provides key management and signing; SS58 address
 * encoding/decoding.
 */
public class SS58 {
    private static final byte[] SS58_PREFIX = "SS58PRE".getBytes();
    
    private SS58() {}

    public static String encode(byte[] publicKey, int prefix) {
        if (publicKey.length != 32) {
            throw new IllegalArgumentException("SS58 only supports 32-byte public keys");
        }

        byte[] data = new byte[1 + publicKey.length];
        data[0] = (byte) prefix;
        System.arraycopy(publicKey, 0, data, 1, publicKey.length);

        byte[] hash = Hasher.hash512(SS58_PREFIX, data);
        byte[] checksum = Arrays.copyOfRange(hash, 0, 2);

        byte[] full = new byte[data.length + 2];
        System.arraycopy(data, 0, full, 0, data.length);
        System.arraycopy(checksum, 0, full, data.length, 2);

        return Base58.encode(full);
    }

    public static byte[] decode(String ss58Address) {
        byte[] data = Base58.decode(ss58Address);
        if (data.length != 35) {
            throw new IllegalArgumentException("Invalid SS58 address length");
        }

        byte[] prefixPlusPubkey = Arrays.copyOfRange(data, 0, 33);
        byte[] checksum = Arrays.copyOfRange(data, 33, 35);

        byte[] hash = Hasher.hash512(SS58_PREFIX, prefixPlusPubkey);
        byte[] expected = Arrays.copyOfRange(hash, 0, 2);

        if (!Arrays.equals(checksum, expected)) {
            throw new IllegalArgumentException("Invalid SS58 checksum");
        }

        return Arrays.copyOfRange(data, 1, 33);
    }
}
