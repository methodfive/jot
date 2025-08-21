package com.method5.jot.signing;

import com.method5.jot.crypto.Sr25519;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Sr25519SignerTest {
    @Test
    void testConstructorRejectsInvalidSeed() {
        byte[] tooShort = new byte[16];
        assertThrows(IllegalArgumentException.class, () -> new Sr25519Signer(tooShort));

        byte[] nullSeed = null;
        assertThrows(IllegalArgumentException.class, () -> new Sr25519Signer(nullSeed));
    }

    @Test
    void testGetPublicKeyAndSeed() {
        byte[][] mnemonicAndSeed = Sr25519.generateMnemonicAndSeed();
        byte[] seed = mnemonicAndSeed[1];

        Sr25519Signer signer = new Sr25519Signer(seed);
        byte[] pubKey = signer.getPublicKey();

        assertNotNull(pubKey);
        assertEquals(32, pubKey.length);
        assertArrayEquals(seed, signer.getSeed());
    }

    @Test
    void testSignAndVerify() {
        byte[][] mnemonicAndSeed = Sr25519.generateMnemonicAndSeed();
        byte[] seed = mnemonicAndSeed[1];

        Sr25519Signer signer = new Sr25519Signer(seed);
        byte[] message = "verify this".getBytes();

        byte[] signature = signer.sign(message);
        byte[] pubKey = signer.getPublicKey();

        assertNotNull(signature);
        assertEquals(64, signature.length);

        boolean valid = Sr25519.verify(pubKey, message, signature);
        assertTrue(valid);
    }

    @Test
    void testVerifyFailsWithTamperedMessage() {
        byte[][] mnemonicAndSeed = Sr25519.generateMnemonicAndSeed();
        byte[] seed = mnemonicAndSeed[1];

        Sr25519Signer signer = new Sr25519Signer(seed);
        byte[] message = "authentic".getBytes();
        byte[] tampered = "forged".getBytes();

        byte[] signature = signer.sign(message);
        byte[] pubKey = signer.getPublicKey();

        assertFalse(Sr25519.verify(pubKey, tampered, signature));
    }
}
