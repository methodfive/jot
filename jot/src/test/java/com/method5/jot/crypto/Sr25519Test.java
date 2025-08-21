package com.method5.jot.crypto;

import com.method5.jot.signing.SigningProvider;
import com.method5.jot.signing.Sr25519Signer;
import com.method5.jot.util.HexUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class Sr25519Test {
    private static byte[] seed;
    private static byte[] publicKey;

    @BeforeAll
    static void setup() {
        byte[][] mnemonicAndSeed = Sr25519.generateMnemonicAndSeed();

        assertNotNull(mnemonicAndSeed);
        assertEquals(2, mnemonicAndSeed.length);
        assertNotNull(mnemonicAndSeed[0]);
        assertNotNull(mnemonicAndSeed[1]);
        assertEquals(32, mnemonicAndSeed[1].length);

        seed = mnemonicAndSeed[1];
        publicKey = Sr25519.derivePublicKey(seed);

        assertNotNull(publicKey);
        assertEquals(32, publicKey.length);
    }

    @Test
    void testClassLoadsWithoutError() {
        assertDoesNotThrow(() -> Class.forName("com.method5.jot.crypto.Sr25519"));
    }

    @Test
    public void testSignVerifyHex() throws Exception {
        byte[][] mnemonicAndSeed = Sr25519.generateMnemonicAndSeed();
        SigningProvider signer = new Sr25519Signer(mnemonicAndSeed[1]);

        byte[] payload = HexUtil.hexToBytes("0102030405");
        byte[] signature = signer.sign(payload);

        assertTrue(Sr25519.verify(signer.getPublicKey(), payload, signature));
    }

    @Test
    public void testSignNotVerify() throws Exception {
        byte[][] mnemonicAndSeed = Sr25519.generateMnemonicAndSeed();
        SigningProvider signer = new Sr25519Signer(mnemonicAndSeed[1]);

        byte[] payload = "test".getBytes(StandardCharsets.UTF_8);
        byte[] signature = signer.sign(payload);

        assertTrue(Sr25519.verify(signer.getPublicKey(), payload, signature));
        assertFalse(Sr25519.verify(signer.getPublicKey(), payload, new byte[12]));
        assertFalse(Sr25519.verify(signer.getPublicKey(), new byte[12], signature));
        assertFalse(Sr25519.verify(new byte[32], payload, signature));
    }

    @Test
    void testDerivePublicKeyFromSeed() {
        byte[] derived = Sr25519.derivePublicKey(seed);
        assertArrayEquals(publicKey, derived);
    }

    @Test
    void testDeriveSeedFromMnemonicMatchesOriginalSeed() {
        byte[][] mnemonicAndSeed = Sr25519.generateMnemonicAndSeed();
        String mnemonic = new String(mnemonicAndSeed[0]);
        byte[] expectedSeed = mnemonicAndSeed[1];

        byte[] rederivedSeed = Sr25519.deriveSeedFromMnemonic(mnemonic, "");
        assertNotNull(rederivedSeed);
        assertEquals(32, rederivedSeed.length);
        assertArrayEquals(expectedSeed, rederivedSeed);
    }
}
