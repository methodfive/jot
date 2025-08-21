package com.method5.jot.crypto;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class HasherTest {
    @Test
    void testHash256Produces32Bytes() {
        byte[] input = "hello world".getBytes(StandardCharsets.UTF_8);
        byte[] hash = Hasher.hash256(input);

        assertNotNull(hash);
        assertEquals(32, hash.length);
    }

    @Test
    void testHash512WithPrefixProducesCorrectLength() {
        byte[] prefix = "prefix".getBytes(StandardCharsets.UTF_8);
        byte[] input = "data".getBytes(StandardCharsets.UTF_8);
        byte[] hash = Hasher.hash512(prefix, input);

        assertNotNull(hash);
        assertEquals(64, hash.length);
    }

    @Test
    void testHash128Produces16Bytes() {
        byte[] input = "input".getBytes(StandardCharsets.UTF_8);
        byte[] hash = Hasher.hash128(input);

        assertNotNull(hash);
        assertEquals(16, hash.length);
    }

    @Test
    void testHash128ConcatConcatenatesCorrectly() {
        byte[] input = "input".getBytes(StandardCharsets.UTF_8);
        byte[] result = Hasher.hash128Concat(input);
        assertNotNull(result);
        assertEquals(16 + input.length, result.length);

        byte[] expectedPrefix = Hasher.hash128(input);
        assertArrayEquals(expectedPrefix, Arrays.copyOfRange(result, 0, 16));
        assertArrayEquals(input, Arrays.copyOfRange(result, 16, result.length));
    }

    @Test
    void testTwoX128FromStringMatchesByteInput() {
        String str = "someKey";
        byte[] fromString = Hasher.twox128(str);
        byte[] fromBytes = Hasher.twox128(str.getBytes(StandardCharsets.UTF_8));

        assertArrayEquals(fromBytes, fromString);
        assertEquals(16, fromString.length);
    }

    @Test
    void testTwoX128Produces16Bytes() {
        byte[] input = "anotherKey".getBytes(StandardCharsets.UTF_8);
        byte[] hash = Hasher.twox128(input);

        assertNotNull(hash);
        assertEquals(16, hash.length);
    }

    @Test
    void testTwoX128IsDeterministic() {
        byte[] input = "fixed".getBytes(StandardCharsets.UTF_8);
        byte[] h1 = Hasher.twox128(input);
        byte[] h2 = Hasher.twox128(input);

        assertArrayEquals(h1, h2);
    }
}

