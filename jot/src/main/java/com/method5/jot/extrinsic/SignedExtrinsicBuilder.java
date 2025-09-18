package com.method5.jot.extrinsic;

import com.method5.jot.entity.Mortality;
import com.method5.jot.entity.MultiAddress;
import com.method5.jot.entity.MultiSignature;
import com.method5.jot.scale.ScaleWriter;
import com.method5.jot.signing.SigningProvider;
import com.method5.jot.util.HexUtil;

import java.math.BigInteger;

/**
 * SignedExtrinsicBuilder — class for signed extrinsic builder in the Jot SDK. Provides key
 * management and signing; extrinsic construction and submission.
 */
public class SignedExtrinsicBuilder {
    public static byte[] build(
        byte[] callData,
        SigningProvider signingProvider,
        Mortality mortality,
        byte[] signature,
        BigInteger nonce,
        BigInteger tip
    ) {
        ScaleWriter writer = new ScaleWriter();
        writer.writeByte((byte) 0x84); // Signed + v4

        // Signature: Address + Signature + Era + Nonce + Tip + Call Data
        writer.writeBytes(new MultiAddress(signingProvider.getPublicKey()).encode());
        writer.writeBytes(new MultiSignature(signingProvider.getKeyType(), HexUtil.bytesToHex(signature)).encode());
        writer.writeBytes(mortality.encode());
        writer.writeCompact(nonce);
        writer.writeCompact(tip);
        writer.writeByte(0); // mode

        writer.writeBytes(callData);

        byte[] raw = writer.toByteArray();
        ScaleWriter outer = new ScaleWriter();
        outer.writeCompact(BigInteger.valueOf(raw.length));
        outer.writeBytes(raw);
        return outer.toByteArray();
    }
}
