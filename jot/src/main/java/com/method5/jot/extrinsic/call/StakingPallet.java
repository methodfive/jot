package com.method5.jot.extrinsic.call;

import com.method5.jot.entity.MultiAddress;
import com.method5.jot.entity.RewardDestination;
import com.method5.jot.metadata.CallIndexResolver;
import com.method5.jot.query.model.AccountId;
import com.method5.jot.scale.ScaleWriter;
import com.method5.jot.util.UnitConverter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * StakingPallet — class for staking pallet in the Jot SDK. Provides extrinsic construction and
 * submission; pallet call builders.
 */
public class StakingPallet {
    public static byte[] bond(CallIndexResolver resolver, BigDecimal amount, RewardDestination rewardDestination) {
        ScaleWriter writer = new ScaleWriter();
        writer.writeBytes(resolver.resolveCallIndex("Staking", "bond"));
        writer.writeCompact(UnitConverter.toPlanck(amount));
        writer.writeBytes(rewardDestination.encode());
        return writer.toByteArray();
    }

    public static byte[] bondExtra(CallIndexResolver resolver, BigDecimal amount) {
        ScaleWriter writer = new ScaleWriter();
        writer.writeBytes(resolver.resolveCallIndex("Staking", "bond_extra"));
        writer.writeCompact(UnitConverter.toPlanck(amount));
        return writer.toByteArray();
    }

    public static byte[] nominate(CallIndexResolver resolver, List<MultiAddress> targets) {
        ScaleWriter writer = new ScaleWriter();
        writer.writeBytes(resolver.resolveCallIndex("Staking", "nominate"));
        writer.writeCompact(BigInteger.valueOf(targets.size()));
        for (MultiAddress target : targets) {
            writer.writeBytes(target.encode());
        }
        return writer.toByteArray();
    }

    public static byte[] chill(CallIndexResolver resolver) {
        ScaleWriter writer = new ScaleWriter();
        writer.writeBytes(resolver.resolveCallIndex("Staking", "chill"));
        return writer.toByteArray();
    }

    public static byte[] unbond(CallIndexResolver resolver, BigDecimal amount) {

        ScaleWriter writer = new ScaleWriter();
        writer.writeBytes(resolver.resolveCallIndex("Staking", "unbond"));
        writer.writeCompact(UnitConverter.toPlanck(amount));
        return writer.toByteArray();
    }

    public static byte[] rebond(CallIndexResolver resolver, BigDecimal amount) {
        ScaleWriter writer = new ScaleWriter();
        writer.writeBytes(resolver.resolveCallIndex("Staking", "rebond"));
        writer.writeCompact(UnitConverter.toPlanck(amount));
        return writer.toByteArray();
    }

    public static byte[] payoutStakers(CallIndexResolver resolver, AccountId validatorStash, int era) {
        ScaleWriter writer = new ScaleWriter();
        writer.writeBytes(resolver.resolveCallIndex("Staking", "payout_stakers"));
        writer.writeBytes(validatorStash.getPublicKey());
        writer.writeInt(era);
        return writer.toByteArray();
    }
}