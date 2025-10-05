package com.method5.jot.extrinsic.call;

import com.method5.jot.rpc.Api;
import com.method5.jot.rpc.CallOrQuery;
import com.method5.jot.scale.ScaleWriter;

import java.math.BigInteger;
import java.util.List;

/**
 * UtilityPallet — class for utility pallet in the Jot SDK. Provides extrinsic construction and
 * submission; utility helpers; pallet call builders.
 */
public class UtilityPallet extends CallOrQuery {
    public UtilityPallet(Api api) {
        super(api);
    }

    public Call batchAll(List<Call> calls) {
        ScaleWriter writer = new ScaleWriter();
        writer.writeBytes(getResolver().resolveCallIndex("Utility", "batch_all"));
        return new Call(api,  getBatchCall(calls, writer));
    }

    public Call batch(List<Call> calls) {
        ScaleWriter writer = new ScaleWriter();
        writer.writeBytes(getResolver().resolveCallIndex("Utility", "batch"));
        return new Call(api,  getBatchCall(calls, writer));
    }

    public Call forceBatch(List<Call> calls) {
        ScaleWriter writer = new ScaleWriter();
        writer.writeBytes(getResolver().resolveCallIndex("Utility", "force_batch"));
        return new Call(api,  getBatchCall(calls, writer));
    }

    private static byte[] getBatchCall(List<Call> calls, ScaleWriter writer) {
        writer.writeCompact(BigInteger.valueOf(calls.size()));
        for (Call call : calls) {
            writer.writeBytes(call.callData());
        }
        return writer.toByteArray();
    }
}
