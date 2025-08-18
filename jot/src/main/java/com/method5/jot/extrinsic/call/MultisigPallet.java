package com.method5.jot.extrinsic.call;

import com.method5.jot.crypto.Hasher;
import com.method5.jot.metadata.CallIndexResolver;
import com.method5.jot.query.model.AccountId;
import com.method5.jot.scale.ScaleWriter;
import com.method5.jot.util.HexUtil;
import com.method5.jot.entity.Timepoint;
import com.method5.jot.entity.Weight;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MultisigPallet {
    public static byte[] approveAsMulti(
            CallIndexResolver resolver,
            int threshold,
            List<AccountId> otherSignatories,
            Timepoint timepoint,
            byte[] call,
            Weight weight) {
        List<byte[]> sortedSigners = sortSigners(convertSigners(otherSignatories));

        ScaleWriter writer = new ScaleWriter();
        writer.writeBytes(resolver.resolveCallIndex("Multisig", "approve_as_multi"));
        writer.writeU16(threshold);
        writer.writeByteArrayVector(sortedSigners);
        writer.writeOptional(timepoint, Timepoint::encode);
        writer.writeBytes(Hasher.hash256(call));
        writer.writeBytes(weight.encode());
        return writer.toByteArray();
    }

    public static byte[] asMulti(
            CallIndexResolver resolver,
            int threshold,
            List<AccountId> otherSignatories,
            Timepoint timepoint,
            byte[] call,
            Weight weight) {
        List<byte[]> sortedSigners = sortSigners(convertSigners(otherSignatories));

        ScaleWriter writer = new ScaleWriter();
        writer.writeBytes(resolver.resolveCallIndex("Multisig", "as_multi"));
        writer.writeU16(threshold);
        writer.writeByteArrayVector(sortedSigners);
        writer.writeOptional(timepoint, Timepoint::encode);
        writer.writeBytes(call);
        writer.writeBytes(weight.encode());
        return writer.toByteArray();
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
