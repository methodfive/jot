package com.method5.jot.signing;

import com.method5.jot.crypto.Ed25519;
import com.method5.jot.wallet.Wallet;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;

import java.security.KeyPair;
import java.security.interfaces.EdECPrivateKey;

/**
 * Ed25519Signer — class for ed 25519 signer in the Jot SDK. Provides key management and signing.
 */
public class Ed25519Signer extends SigningProvider {
    private final KeyPair keyPair;

    public Ed25519Signer(KeyPair keyPair) {
        super(Wallet.KeyType.ED25519);
        this.keyPair = keyPair;
    }

    @Override
    public byte[] getPublicKey() {
        EdECPrivateKey privateKey = (EdECPrivateKey) this.keyPair.getPrivate();
        byte[] privateKeyBytes = privateKey.getBytes().get();
        Ed25519PrivateKeyParameters priv = new Ed25519PrivateKeyParameters(privateKeyBytes, 0);
        return priv.generatePublicKey().getEncoded();

    }

    @Override
    public byte[] sign(byte[] payload) throws Exception {
        return Ed25519.sign(keyPair.getPrivate(), payload);
    }

    @Override
    public boolean verify(byte[] payload, byte[] signature) {
        return Ed25519.verify(getPublicKey(), payload, signature);
    }
}
