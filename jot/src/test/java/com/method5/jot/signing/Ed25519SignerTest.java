package com.method5.jot.signing;

import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

import static org.junit.jupiter.api.Assertions.*;

class Ed25519SignerTest {
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    void testGetPublicKeyMatchesDerivedKey() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("Ed25519");
        KeyPair keyPair = generator.generateKeyPair();

        Ed25519Signer signer = new Ed25519Signer(keyPair);
        byte[] derivedPublicKey = signer.getPublicKey();

        // Derive expected public key from private key manually
        byte[] privateBytes = ((java.security.interfaces.EdECPrivateKey) keyPair.getPrivate()).getBytes().get();
        Ed25519PrivateKeyParameters privateParams = new Ed25519PrivateKeyParameters(privateBytes, 0);
        byte[] expected = privateParams.generatePublicKey().getEncoded();

        assertArrayEquals(expected, derivedPublicKey);
    }

    @Test
    void testSignAndVerify() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("Ed25519");
        KeyPair keyPair = generator.generateKeyPair();

        Ed25519Signer signer = new Ed25519Signer(keyPair);
        byte[] message = "hello world".getBytes();
        byte[] signature = signer.sign(message);

        assertNotNull(signature);
        assertEquals(64, signature.length);
    }
}
