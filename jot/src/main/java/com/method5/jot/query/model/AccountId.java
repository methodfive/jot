package com.method5.jot.query.model;

import com.method5.jot.wallet.SS58;

/**
 * AccountId — class for account id in the Jot SDK. Provides types and data models.
 */
public final class AccountId {
    private final byte[] publicKey;

    public AccountId(byte[] publicKey) {
        if (publicKey.length != 32) {
            throw new IllegalArgumentException("Invalid public key length");
        }
        this.publicKey = publicKey;
    }

    public static AccountId fromSS58(String ss58Address) {
        return new AccountId(SS58.decode(ss58Address));
    }

    public byte[] getPublicKey() {
        return publicKey;
    }
}
