package com.method5.jot.extrinsic.call;

import com.method5.jot.rpc.Api;

/**
 * Call — Wrapper for all pallets in the Jot SDK.
 */
public class Transaction {
    protected Api api;
    protected BalancesPallet balances;
    protected ConvictionVotingPallet convictionVoting;
    protected MultisigPallet multisig;
    protected StakingPallet staking;
    protected SystemPallet system;
    protected UtilityPallet utility;

    public Transaction(Api api) {
        this.api = api;

        balances = new BalancesPallet(api);
        convictionVoting = new ConvictionVotingPallet(api);
        multisig = new MultisigPallet(api);
        staking = new StakingPallet(api);
        system = new SystemPallet(api);
        utility = new UtilityPallet(api);
    }

    public BalancesPallet balances() {
        return balances;
    }

    public ConvictionVotingPallet convictionVoting() {
        return convictionVoting;
    }

    public MultisigPallet multisig() {
        return multisig;
    }

    public StakingPallet staking() {
        return staking;
    }

    public SystemPallet system() {
        return system;
    }

    public UtilityPallet utility() {
        return utility;
    }
}
