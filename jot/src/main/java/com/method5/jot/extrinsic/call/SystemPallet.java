package com.method5.jot.extrinsic.call;

import com.method5.jot.metadata.CallIndexResolver;
import com.method5.jot.scale.ScaleWriter;

import java.math.BigInteger;

/**
 * SystemPallet — class for system pallet in the Jot SDK. Provides extrinsic construction and
 * submission; pallet call builders.
 */
public final class SystemPallet {
    private SystemPallet() {}

    public static byte[] remark(CallIndexResolver resolver, byte[] message) {
        ScaleWriter writer = new ScaleWriter();
        writer.writeBytes(resolver.resolveCallIndex("System", "remark"));
        writer.writeCompact(BigInteger.valueOf(message.length));
        writer.writeBytes(message);
        return writer.toByteArray();
    }
}