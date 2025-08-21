package com.method5.jot.crypto;

import com.method5.jot.signing.Ed25519Signer;
import com.method5.jot.signing.SigningProvider;
import com.method5.jot.util.HexUtil;
import com.method5.jot.wallet.Wallet;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class Ed25519Test {
    @Test
    void testGenerate() throws Exception {
        Ed25519.KeyPairAndPrivateKeyBytes result = Ed25519.generate();
        assertNotNull(result.keyPair());
        assertNotNull(result.privateKey());
        assertEquals(32, result.privateKey().length);
    }

    @Test
    void testLoadKeyPairFromInvalidPrivateKeyThrows() {
        byte[] tooShort = new byte[16];
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> Ed25519.loadKeyPairFromPrivateKey(tooShort));
        assertEquals("Private key must be 32 bytes (seed)", ex.getMessage());
    }

    @Test
    public void testSignVerifyHex() throws Exception {
        KeyPair keyPair = Ed25519.generate().keyPair();
        SigningProvider signer = new Ed25519Signer(keyPair);

        byte[] payload = HexUtil.hexToBytes("0102030405");
        byte[] signature = signer.sign(payload);

        assertTrue(Ed25519.verify(signer.getPublicKey(), payload, signature));
    }

    @Test
    public void testSignNotVerify() throws Exception {
        KeyPair keyPair = Ed25519.generate().keyPair();
        SigningProvider signer = new Ed25519Signer(keyPair);

        byte[] payload = "test".getBytes(StandardCharsets.UTF_8);
        byte[] signature = signer.sign(payload);

        assertTrue(Ed25519.verify(signer.getPublicKey(), payload, signature));
        assertFalse(Ed25519.verify(signer.getPublicKey(), payload, new byte[64]));
        assertFalse(Ed25519.verify(signer.getPublicKey(), new byte[12], signature));
    }

    @Test
    public void testLoadByPrivateKey() throws Exception {
        Wallet wallet = Wallet.generate(Wallet.KeyType.ED25519);

        Wallet generated = Wallet.fromEd25519PrivateKey(wallet.getPrivateKeyBytes());

        assertArrayEquals(wallet.getKeyPair().getPrivate().getEncoded(), generated.getKeyPair().getPrivate().getEncoded());
        assertArrayEquals(wallet.getKeyPair().getPublic().getEncoded(), generated.getKeyPair().getPublic().getEncoded());
    }

    @Test
    void testFromCompressedPublicKey() {
        // Use a known public key with high bit set on last byte
        byte[] publicKey = new byte[32];
        Arrays.fill(publicKey, (byte) 1);
        publicKey[31] |= (byte) 0x80;

        var point = Ed25519.fromCompressedPublicKey(publicKey);
        assertTrue(point.isXOdd());
        assertNotNull(point.getY());
    }
}
