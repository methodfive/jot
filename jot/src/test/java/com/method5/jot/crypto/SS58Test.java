package com.method5.jot.crypto;

import com.method5.jot.util.HexUtil;
import com.method5.jot.wallet.SS58;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.security.SecureRandom;
import java.util.Arrays;

public class SS58Test {
    private static final int NETWORK_PREFIX = 42;

    @Test
    public void testSS58Encoding() {
        byte[] pubKey = HexUtil.hexToBytes("0x402b1a479c014e80596569cbe5aa0eda7fe41d84640141c8024e57bba8082d30");
        
        assertEquals("12T8sPbDjkP3MafjbKJhZAzHGXMPNDEMqc6H96bvfi5AJGkG", SS58.encode(pubKey, 0));
        assertEquals("5DWqj4L9sy7Zv3fDdgFhR2A8QuMjfugDm7Mnyoca7d3e7oEz", SS58.encode(pubKey, 42));
        assertEquals("E2TPNg2WL8VfhUfQP4kJyX8ZVdyUaVQDVCYNTtXbRG8rxmz", SS58.encode(pubKey, 2));

        assertArrayEquals(pubKey, SS58.decode("12T8sPbDjkP3MafjbKJhZAzHGXMPNDEMqc6H96bvfi5AJGkG"));
        assertArrayEquals(pubKey, SS58.decode("5DWqj4L9sy7Zv3fDdgFhR2A8QuMjfugDm7Mnyoca7d3e7oEz"));
        assertArrayEquals(pubKey, SS58.decode("E2TPNg2WL8VfhUfQP4kJyX8ZVdyUaVQDVCYNTtXbRG8rxmz"));
    }

    @Test
    public void testEncodeDecodeRoundtrip() {
        byte[] pubKey = new byte[32];
        new SecureRandom().nextBytes(pubKey);

        String encoded = SS58.encode(pubKey, 0);
        byte[] decoded = SS58.decode(encoded);

        assertArrayEquals(pubKey, decoded);
    }

    @Test
    void testEncodeThrowsOnInvalidKeyLength() {
        byte[] shortKey = new byte[31];
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                SS58.encode(shortKey, NETWORK_PREFIX)
        );
        assertEquals("SS58 only supports 32-byte public keys", ex.getMessage());
    }

    @Test
    void testDecodeThrowsOnInvalidLength() {
        String invalid = "3KMU5d";
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                SS58.decode(invalid)
        );
        assertEquals("Invalid SS58 address length", ex.getMessage());
    }

    @Test
    void testDecodeThrowsOnInvalidChecksum() {
        byte[] publicKey = new byte[32];
        Arrays.fill(publicKey, (byte) 1);

        String encoded = SS58.encode(publicKey, NETWORK_PREFIX);

        // Corrupt last character to break checksum
        char[] chars = encoded.toCharArray();
        chars[chars.length - 1] = chars[chars.length - 1] == '1' ? '2' : '1';
        String corrupted = new String(chars);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                SS58.decode(corrupted)
        );
        assertEquals("Invalid SS58 checksum", ex.getMessage());
    }
}
