package com.method5.jot.util;

import com.method5.jot.crypto.Hasher;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Base58Test {
    private static final Logger logger = LoggerFactory.getLogger(Base58Test.class);

    @Test
    public void testBase58RoundTripSS58Format() {
        byte[] publicKey = new byte[32];
        new SecureRandom().nextBytes(publicKey);

        byte[] data = new byte[33]; // 1-byte prefix + 32-byte public key
        data[0] = 0; // prefix = 0
        System.arraycopy(publicKey, 0, data, 1, publicKey.length);

        byte[] checksum = Arrays.copyOfRange(Hasher.hash512("SS58PRE".getBytes(), data), 0, 2);

        byte[] full = new byte[35];
        System.arraycopy(data, 0, full, 0, 33);
        System.arraycopy(checksum, 0, full, 33, 2);

        String encoded = Base58.encode(full);
        byte[] decoded = Base58.decode(encoded);

        logger.info("Original: {}", Arrays.toString(full));
        logger.info("Decoded : {}", Arrays.toString(decoded));
        logger.info("Length  : {}", decoded.length);

        assertEquals(35, decoded.length, "Decoded length must be 35 bytes");
        assertArrayEquals(full, decoded, "Base58 round-trip failed");
    }
}
