package com.method5.jot.extrinsic.call;

import com.method5.jot.crypto.Hasher;
import com.method5.jot.entity.Timepoint;
import com.method5.jot.entity.Weight;
import com.method5.jot.query.model.AccountId;
import com.method5.jot.rpc.Api;
import com.method5.jot.rpc.CallOrQuery;
import com.method5.jot.scale.ScaleWriter;
import com.method5.jot.util.HexUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * MultisigPallet — class for multisig pallet in the Jot SDK. Provides key management and signing;
 * extrinsic construction and submission; pallet call builders.
 */
public class MultisigPallet extends CallOrQuery {
    public MultisigPallet(Api api) {
        super(api);
    }

    public Call approveAsMulti(
            int threshold,
            List<AccountId> otherSignatories,
            Timepoint timepoint,
            Call call,
            Weight weight) {
        List<byte[]> sortedSigners = sortSigners(convertSigners(otherSignatories));

        ScaleWriter writer = new ScaleWriter();
        writer.writeBytes(getResolver().resolveCallIndex("Multisig", "approve_as_multi"));
        writer.writeU16(threshold);
        writer.writeByteArrayVector(sortedSigners);
        writer.writeOptional(timepoint, Timepoint::encode);
        writer.writeBytes(Hasher.hash256(call.callData()));
        writer.writeBytes(weight.encode());
        return new Call(api, writer.toByteArray());
    }

    public Call asMulti(
            int threshold,
            List<AccountId> otherSignatories,
            Timepoint timepoint,
            Call call,
            Weight weight) {
        List<byte[]> sortedSigners = sortSigners(convertSigners(otherSignatories));

        ScaleWriter writer = new ScaleWriter();
        writer.writeBytes(getResolver().resolveCallIndex("Multisig", "as_multi"));
        writer.writeU16(threshold);
        writer.writeByteArrayVector(sortedSigners);
        writer.writeOptional(timepoint, Timepoint::encode);
        writer.writeBytes(call.callData());
        writer.writeBytes(weight.encode());
        return new Call(api, writer.toByteArray());
    }

    private static List<byte[]> convertSigners(List<AccountId> otherSignatories) {
        List<byte[]> result = new ArrayList<>();
        for (AccountId id : otherSignatories) {
            result.add(id.getPublicKey());
        }
        return result;
    }

    private static List<byte[]> sortSigners(List<byte[]> signers) {
        signers.sort(Comparator.comparing(HexUtil::bytesToHex));
        return signers;
    }
}
