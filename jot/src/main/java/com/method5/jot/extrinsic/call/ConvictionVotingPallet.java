package com.method5.jot.extrinsic.call;

import com.method5.jot.entity.MultiAddress;
import com.method5.jot.extrinsic.model.AccountVote;
import com.method5.jot.extrinsic.model.Conviction;
import com.method5.jot.rpc.Api;
import com.method5.jot.rpc.CallOrQuery;
import com.method5.jot.scale.ScaleWriter;
import com.method5.jot.util.UnitConverter;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * ConvictionVotingPallet — class for conviction voting pallet in the Jot SDK. Provides extrinsic
 * construction and submission; pallet call builders.
 */
public class ConvictionVotingPallet extends CallOrQuery {
    public ConvictionVotingPallet(Api api) {
        super(api);
    }

    public Call vote(int referendumIndex, AccountVote vote) {
        ScaleWriter writer = new ScaleWriter();
        writer.writeBytes(getResolver().resolveCallIndex("ConvictionVoting", "vote"));
        writer.writeCompact(BigInteger.valueOf(referendumIndex));
        writer.writeBytes(vote.encode());
        return new Call(api, writer.toByteArray());
    }

    public Call delegate(int classOf, MultiAddress target, Conviction conviction, BigDecimal balance) {
        ScaleWriter writer = new ScaleWriter();
        writer.writeBytes(getResolver().resolveCallIndex("ConvictionVoting", "delegate"));
        writer.writeU16(classOf);
        writer.writeBytes(target.encode());
        writer.writeByte(conviction.index());
        writer.writeU128(UnitConverter.toPlanck(balance));
        return new Call(api, writer.toByteArray());
    }

    public Call undelegate(int classOf) {
        ScaleWriter writer = new ScaleWriter();
        writer.writeBytes(getResolver().resolveCallIndex("ConvictionVoting", "undelegate"));
        writer.writeU16(classOf);
        return new Call(api, writer.toByteArray());
    }

    public Call unlock(int classOf, MultiAddress target) {
        ScaleWriter writer = new ScaleWriter();
        writer.writeBytes(getResolver().resolveCallIndex("ConvictionVoting", "unlock"));
        writer.writeU16(classOf);
        writer.writeBytes(target.encode());
        return new Call(api, writer.toByteArray());
    }

    public Call removeVote(Integer classOf, int referendumIndex) {
        ScaleWriter writer = new ScaleWriter();
        writer.writeBytes(getResolver().resolveCallIndex("ConvictionVoting", "remove_vote"));
        if(classOf == null) {
            writer.writeByte(0);
        } else {
            writer.writeByte(1);
            writer.writeU16(classOf);
        }
        writer.writeInt(referendumIndex);
        return new Call(api, writer.toByteArray());
    }

    public Call removeOtherVote(MultiAddress target, int classOf, int referendumIndex) {
        ScaleWriter writer = new ScaleWriter();
        writer.writeBytes(getResolver().resolveCallIndex("ConvictionVoting", "remove_other_vote"));
        writer.writeBytes(target.encode());
        writer.writeU16(classOf);
        writer.writeInt(referendumIndex);
        return new Call(api, writer.toByteArray());
    }
}