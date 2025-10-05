package com.method5.jot.extrinsic.call;

import com.method5.jot.rpc.Api;
import com.method5.jot.rpc.CallOrQuery;
import com.method5.jot.scale.ScaleWriter;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

/**
 * SystemPallet — class for system pallet in the Jot SDK. Provides extrinsic construction and
 * submission; pallet call builders.
 */
public class SystemPallet extends CallOrQuery {
    public SystemPallet(Api api) {
        super(api);
    }

    public Call remark(String remark) {
        return remark(remark.getBytes(StandardCharsets.UTF_8));
    }

    public Call remark(byte[] message) {
        ScaleWriter writer = new ScaleWriter();
        writer.writeBytes(getResolver().resolveCallIndex("System", "remark"));
        writer.writeCompact(BigInteger.valueOf(message.length));
        writer.writeBytes(message);
        return new Call(api, writer.toByteArray());
    }
}