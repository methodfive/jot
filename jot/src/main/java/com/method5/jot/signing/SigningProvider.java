package com.method5.jot.signing;

import com.method5.jot.wallet.Wallet;

public abstract class SigningProvider {
    protected final Wallet.KeyType keyType;

    public SigningProvider(Wallet.KeyType keyType) {
        this.keyType = keyType;
    }

    public abstract byte[] getPublicKey();
    public abstract byte[] sign(byte[] payload) throws Exception;
    public abstract boolean verify(byte[] payload, byte[] signature);

    public Wallet.KeyType getKeyType() {
        return keyType;
    }
}
