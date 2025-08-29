package com.method5.jot.entity;

import com.method5.jot.scale.ScaleReader;
import com.method5.jot.util.HexUtil;
import com.method5.jot.wallet.Wallet;

/**
 * MultiSignature — class for multi signature in the Jot SDK. Provides key management and signing;
 * types and data models.
 */
public class MultiSignature {
    private Wallet.KeyType type;
    private String signature;

    public MultiSignature(Wallet.KeyType type, String signature) {
        this.type = type;
        this.signature = signature;
    }

    public MultiSignature() {
    }

    public static MultiSignature decode(ScaleReader reader) {
        MultiSignature signature = new MultiSignature();
        byte type = reader.readByte();
        if(type == 0) {
            signature.setType(Wallet.KeyType.ED25519);
        } else if(type == 1) {
            signature.setType(Wallet.KeyType.SR25519);
        } else if(type == 2) {
            signature.setType(Wallet.KeyType.ECDSA);
        }
        signature.setSignature(HexUtil.bytesToHex(reader.readBytes(64)));
        return signature;
    }

    public byte[] encode() {
        byte[] signatureBytes = HexUtil.hexToBytes(signature);
        if (signatureBytes.length != 64) {
            throw new IllegalArgumentException("Signature must be 64 bytes");
        }

        byte[] result = new byte[65];

        if(type == Wallet.KeyType.ED25519) {
           result[0] = 0;
        } else if(type == Wallet.KeyType.SR25519) {
            result[0] = 1;
        } else if(type == Wallet.KeyType.ECDSA) {
            result[0] = 2;
        }

        System.arraycopy(signatureBytes, 0, result, 1, 64);
        return result;
    }

    public Wallet.KeyType getType() {
        return type;
    }

    public void setType(Wallet.KeyType type) {
        this.type = type;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        return "MultiSignature{" +
                "type=" + type +
                ", signature='" + signature + '\'' +
                '}';
    }
}
