package com.method5.jot.crypto;

import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.EdECPrivateKey;
import java.security.spec.EdECPoint;
import java.security.spec.EdECPrivateKeySpec;
import java.security.spec.EdECPublicKeySpec;
import java.security.spec.NamedParameterSpec;

/**
 * Ed25519 — class for ed 25519 in the Jot SDK. Provides key management and signing.
 */
public final class Ed25519 {
    static {
        if (Security.getProvider("BC") == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    private Ed25519() {}

    public static byte[] sign(PrivateKey privateKey, byte[] payload) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("Ed25519");
        signature.initSign(privateKey);
        signature.update(payload);
        return signature.sign();
    }

    public record KeyPairAndPrivateKeyBytes(KeyPair keyPair, byte[] privateKey) {
    }

    public static KeyPairAndPrivateKeyBytes generate() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("Ed25519");
        KeyPair originalKeyPair = kpg.generateKeyPair();

        byte[] privateKeyBytes = ((EdECPrivateKey) originalKeyPair.getPrivate()).getBytes().get();
        return new KeyPairAndPrivateKeyBytes(loadKeyPairFromPrivateKey(privateKeyBytes), privateKeyBytes);
    }

    public static KeyPair loadKeyPairFromPrivateKey(byte[] privateKeyBytes) throws Exception {
        if (privateKeyBytes.length != 32) {
            throw new IllegalArgumentException("Private key must be 32 bytes (seed)");
        }

        Ed25519PrivateKeyParameters privateParams = new Ed25519PrivateKeyParameters(privateKeyBytes, 0);
        Ed25519PublicKeyParameters publicParams = privateParams.generatePublicKey();

        byte[] publicKeyBytes = publicParams.getEncoded(); // 32 bytes

        // Construct standard Java keys from raw bytes
        KeyFactory keyFactory = KeyFactory.getInstance("Ed25519");
        NamedParameterSpec paramSpec = new NamedParameterSpec("Ed25519");

        // PrivateKey
        EdECPrivateKeySpec privateSpec = new EdECPrivateKeySpec(paramSpec, privateKeyBytes);
        PrivateKey privateKey = keyFactory.generatePrivate(privateSpec);

        // PublicKey
        EdECPoint point = fromCompressedPublicKey(publicKeyBytes);
        EdECPublicKeySpec pubSpec = new EdECPublicKeySpec(new NamedParameterSpec("Ed25519"), point);
        PublicKey publicKey = keyFactory.generatePublic(pubSpec);

        return new KeyPair(publicKey, privateKey);
    }

    public static EdECPoint fromCompressedPublicKey(byte[] pk) {
        boolean xOdd = (pk[31] & 0x80) != 0;
        pk[31] &= 0x7F;
        BigInteger y = new BigInteger(1, pk);
        return new EdECPoint(xOdd, y);
    }

    public static boolean verify(byte[] publicKey, byte[] payload, byte[] signature) {
        if (publicKey == null || publicKey.length != 32)
            throw new IllegalArgumentException("Invalid Ed25519 public key (must be 32 bytes)");
        if (signature == null || signature.length != 64)
            throw new IllegalArgumentException("Invalid Ed25519 signature (must be 64 bytes)");

        Ed25519PublicKeyParameters pubKey = new Ed25519PublicKeyParameters(publicKey, 0);
        org.bouncycastle.crypto.signers.Ed25519Signer signer = new org.bouncycastle.crypto.signers.Ed25519Signer();
        signer.init(false, pubKey);
        signer.update(payload, 0, payload.length);
        return signer.verifySignature(signature);
    }
}
