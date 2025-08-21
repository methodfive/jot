package com.method5.jot.crypto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Blake2bTest {
    @Test
    public void testHashLength() {
        byte[] hash = Hasher.hash512("SS58PRE".getBytes(), new byte[33]);
        assertEquals(64, hash.length);
    }

    @Test
    public void testHashDeterminism() {
        byte[] input = new byte[] {1, 2, 3};
        byte[] hash1 = Hasher.hash512("prefix".getBytes(), input);
        byte[] hash2 = Hasher.hash512("prefix".getBytes(), input);
        assertArrayEquals(hash1, hash2);
    }
}
