package com.method5.jot.extrinsic.call;

import com.method5.jot.metadata.CallIndexResolver;
import com.method5.jot.scale.ScaleWriter;

import java.math.BigInteger;
import java.util.List;

public class UtilityPallet {
    public static byte[] batchAll(CallIndexResolver resolver, List<byte[]> calls) {
        ScaleWriter writer = new ScaleWriter();
        writer.writeBytes(resolver.resolveCallIndex("Utility", "batch_all"));
        return getBatchCall(calls, writer);
    }

    public static byte[] batch(CallIndexResolver resolver, List<byte[]> calls) {
        ScaleWriter writer = new ScaleWriter();
        writer.writeBytes(resolver.resolveCallIndex("Utility", "batch"));
        return getBatchCall(calls, writer);
    }

    public static byte[] forceBatch(CallIndexResolver resolver, List<byte[]> calls) {
        ScaleWriter writer = new ScaleWriter();
        writer.writeBytes(resolver.resolveCallIndex("Utility", "force_batch"));
        return getBatchCall(calls, writer);
    }

    private static byte[] getBatchCall(List<byte[]> calls, ScaleWriter writer) {
        writer.writeCompact(BigInteger.valueOf(calls.size()));
        for (byte[] call : calls) {
            writer.writeBytes(call);
        }
        return writer.toByteArray();
    }
}
