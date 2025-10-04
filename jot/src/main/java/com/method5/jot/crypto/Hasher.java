package com.method5.jot.crypto;

import net.jpountz.xxhash.XXHashFactory;
import org.bouncycastle.crypto.digests.Blake2bDigest;

import java.nio.charset.StandardCharsets;

/**
 * Hasher — class for hasher in the Jot SDK.
 */
public final class Hasher {
    private Hasher() {}

    private static final XXHashFactory XX = XXHashFactory.fastestInstance();

    public static byte[] hash256(byte[] input) {
        Blake2bDigest digest = new Blake2bDigest(256);
        digest.update(input, 0, input.length);

        byte[] result = new byte[32];
        digest.doFinal(result, 0);
        return result;
    }

    public static byte[] hash512(byte[] prefix, byte[] input) {
        Blake2bDigest digest = new Blake2bDigest(512);
        digest.update(prefix, 0, prefix.length);
        digest.update(input, 0, input.length);

        byte[] out = new byte[digest.getDigestSize()];
        digest.doFinal(out, 0);
        return out;
    }

    public static byte[] hash128(byte[] input) {
        Blake2bDigest digest = new Blake2bDigest(128);
        digest.update(input, 0, input.length);

        byte[] out = new byte[16];
        digest.doFinal(out, 0);
        return out;
    }

    public static byte[] hash128Concat(byte[] input) {
        byte[] hash = hash128(input);
        byte[] result = new byte[hash.length + input.length];
        System.arraycopy(hash, 0, result, 0, hash.length);
        System.arraycopy(input, 0, result, hash.length, input.length);
        return result;
    }

    public static byte[] twox128(String input) {
        return twox128(input.getBytes(StandardCharsets.UTF_8));
    }

    public static byte[] twox128(byte[] input) {
        long h1 = XX.hash64().hash(input, 0, input.length, 0);
        long h2 = XX.hash64().hash(input, 0, input.length, 1);

        byte[] result = new byte[16];
        for (int i = 0; i < 8; i++) {
            result[i] = (byte) (h1 >>> (i * 8));
            result[i + 8] = (byte) (h2 >>> (i * 8));
        }
        return result;
    }
}
