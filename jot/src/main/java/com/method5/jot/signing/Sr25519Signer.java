package com.method5.jot.signing;

import com.method5.jot.crypto.Sr25519;
import com.method5.jot.wallet.Wallet;

public class Sr25519Signer extends SigningProvider {
    private final byte[] seed;

    public Sr25519Signer(byte[] seed) {
        super(Wallet.KeyType.SR25519);

        if (seed == null || seed.length != 32) {
            throw new IllegalArgumentException("Seed must be 32 bytes");
        }
        this.seed = seed;
    }

    @Override
    public byte[] getPublicKey() {
        return Sr25519.derivePublicKey(seed);
    }

    @Override
    public byte[] sign(byte[] payload) {
        return Sr25519.sign(seed, payload);
    }

    @Override
    public boolean verify(byte[] payload, byte[] signature) {
        return Sr25519.verify(getPublicKey(), payload, signature);
    }

    public byte[] getSeed() {
        return seed;
    }
}
