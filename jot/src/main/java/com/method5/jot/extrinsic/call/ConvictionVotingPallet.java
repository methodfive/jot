package com.method5.jot.extrinsic.call;

import com.method5.jot.entity.MultiAddress;
import com.method5.jot.extrinsic.model.AccountVote;
import com.method5.jot.extrinsic.model.Conviction;
import com.method5.jot.metadata.CallIndexResolver;
import com.method5.jot.scale.ScaleWriter;
import com.method5.jot.util.UnitConverter;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * ConvictionVotingPallet — class for conviction voting pallet in the Jot SDK. Provides extrinsic
 * construction and submission; pallet call builders.
 */
public class ConvictionVotingPallet {
    public static byte[] vote(CallIndexResolver resolver, int referendumIndex, AccountVote vote) {
        ScaleWriter writer = new ScaleWriter();
        writer.writeBytes(resolver.resolveCallIndex("ConvictionVoting", "vote"));
        writer.writeCompact(BigInteger.valueOf(referendumIndex));
        writer.writeBytes(vote.encode());
        return writer.toByteArray();
    }

    public static byte[] delegate(CallIndexResolver resolver, int classOf, MultiAddress target, Conviction conviction, BigDecimal balance) {
        ScaleWriter writer = new ScaleWriter();
        writer.writeBytes(resolver.resolveCallIndex("ConvictionVoting", "delegate"));
        writer.writeU16(classOf);
        writer.writeBytes(target.encode());
        writer.writeByte(conviction.index());
        writer.writeU128(UnitConverter.toPlanck(balance));
        return writer.toByteArray();
    }

    public static byte[] undelegate(CallIndexResolver resolver, int classOf) {
        ScaleWriter writer = new ScaleWriter();
        writer.writeBytes(resolver.resolveCallIndex("ConvictionVoting", "undelegate"));
        writer.writeU16(classOf);
        return writer.toByteArray();
    }

    public static byte[] unlock(CallIndexResolver resolver, int classOf, MultiAddress target) {
        ScaleWriter writer = new ScaleWriter();
        writer.writeBytes(resolver.resolveCallIndex("ConvictionVoting", "unlock"));
        writer.writeU16(classOf);
        writer.writeBytes(target.encode());
        return writer.toByteArray();
    }

    public static byte[] removeVote(CallIndexResolver resolver, Integer classOf, int referendumIndex) {
        ScaleWriter writer = new ScaleWriter();
        writer.writeBytes(resolver.resolveCallIndex("ConvictionVoting", "remove_vote"));
        if(classOf == null) {
            writer.writeByte(0);
        } else {
            writer.writeByte(1);
            writer.writeU16(classOf);
        }
        writer.writeInt(referendumIndex);
        return writer.toByteArray();
    }

    public static byte[] removeOtherVote(CallIndexResolver resolver, MultiAddress target, int classOf, int referendumIndex) {
        ScaleWriter writer = new ScaleWriter();
        writer.writeBytes(resolver.resolveCallIndex("ConvictionVoting", "remove_other_vote"));
        writer.writeBytes(target.encode());
        writer.writeU16(classOf);
        writer.writeInt(referendumIndex);
        return writer.toByteArray();
    }
}