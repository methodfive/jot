package com.method5.jot.extrinsic.call;

import com.method5.jot.entity.MultiAddress;
import com.method5.jot.entity.RewardDestination;
import com.method5.jot.query.model.AccountId;
import com.method5.jot.rpc.Api;
import com.method5.jot.rpc.CallOrQuery;
import com.method5.jot.scale.ScaleWriter;
import com.method5.jot.util.UnitConverter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * StakingPallet — class for staking pallet in the Jot SDK. Provides extrinsic construction and
 * submission; pallet call builders.
 */
public class StakingPallet extends CallOrQuery {
    public StakingPallet(Api api) {
        super(api);
    }

    public Call bond(BigDecimal amount, RewardDestination rewardDestination) {
        ScaleWriter writer = new ScaleWriter();
        writer.writeBytes(getResolver().resolveCallIndex("Staking", "bond"));
        writer.writeCompact(UnitConverter.toPlanck(amount));
        writer.writeBytes(rewardDestination.encode());
        return new Call(api, writer.toByteArray());
    }

    public Call bondExtra(BigDecimal amount) {
        ScaleWriter writer = new ScaleWriter();
        writer.writeBytes(getResolver().resolveCallIndex("Staking", "bond_extra"));
        writer.writeCompact(UnitConverter.toPlanck(amount));
        return new Call(api, writer.toByteArray());
    }

    public Call nominate(List<MultiAddress> targets) {
        ScaleWriter writer = new ScaleWriter();
        writer.writeBytes(getResolver().resolveCallIndex("Staking", "nominate"));
        writer.writeCompact(BigInteger.valueOf(targets.size()));
        for (MultiAddress target : targets) {
            writer.writeBytes(target.encode());
        }
        return new Call(api, writer.toByteArray());
    }

    public Call chill() {
        ScaleWriter writer = new ScaleWriter();
        writer.writeBytes(getResolver().resolveCallIndex("Staking", "chill"));
        return new Call(api, writer.toByteArray());
    }

    public Call unbond(BigDecimal amount) {

        ScaleWriter writer = new ScaleWriter();
        writer.writeBytes(getResolver().resolveCallIndex("Staking", "unbond"));
        writer.writeCompact(UnitConverter.toPlanck(amount));
        return new Call(api, writer.toByteArray());
    }

    public Call rebond(BigDecimal amount) {
        ScaleWriter writer = new ScaleWriter();
        writer.writeBytes(getResolver().resolveCallIndex("Staking", "rebond"));
        writer.writeCompact(UnitConverter.toPlanck(amount));
        return new Call(api, writer.toByteArray());
    }

    public Call payoutStakers(AccountId validatorStash, int era) {
        ScaleWriter writer = new ScaleWriter();
        writer.writeBytes(getResolver().resolveCallIndex("Staking", "payout_stakers"));
        writer.writeBytes(validatorStash.getPublicKey());
        writer.writeInt(era);
        return new Call(api, writer.toByteArray());
    }
}