package com.method5.jot.extrinsic.call;

import com.method5.jot.entity.MultiAddress;
import com.method5.jot.query.model.AccountId;
import com.method5.jot.rpc.Api;
import com.method5.jot.rpc.CallOrQuery;
import com.method5.jot.scale.ScaleWriter;
import com.method5.jot.util.UnitConverter;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * BalancesPallet — class for balances pallet in the Jot SDK. Provides extrinsic construction and
 * submission; pallet call builders.
 */
public class BalancesPallet extends CallOrQuery {
    public BalancesPallet(Api api) {
        super(api);
    }

    public Call transferKeepAlive(AccountId accountId, BigDecimal amount) {
        return new Call(api, buildTransfer(
                        getResolver().resolveCallIndex("Balances", "transfer_keep_alive"),
                        accountId,
                        UnitConverter.toPlanck(amount)));
    }

    public Call transferAllowDeath(AccountId accountId, BigDecimal amount) {
        return new Call(api,  buildTransfer(
                getResolver().resolveCallIndex("Balances", "transfer_allow_death"),
                accountId,
                UnitConverter.toPlanck(amount)));
    }

    public Call transferAll(AccountId accountId, boolean keepAlive) {
        ScaleWriter writer = new ScaleWriter();
        writer.writeBytes(getResolver().resolveCallIndex("Balances", "transfer_all"));
        writer.writeBytes(MultiAddress.fromAccountId(accountId).encode());
        writer.writeBoolean(keepAlive);
        return new Call(api, writer.toByteArray());
    }

    private byte[] buildTransfer(byte[] callIndex, AccountId destination, BigInteger amount) {
        ScaleWriter writer = new ScaleWriter();
        writer.writeBytes(callIndex);
        writer.writeBytes(MultiAddress.fromAccountId(destination).encode());
        writer.writeCompact(amount);
        return writer.toByteArray();
    }
}
