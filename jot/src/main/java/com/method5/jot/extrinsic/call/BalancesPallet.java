package com.method5.jot.extrinsic.call;

import com.method5.jot.metadata.CallIndexResolver;
import com.method5.jot.query.model.AccountId;
import com.method5.jot.scale.ScaleWriter;
import com.method5.jot.entity.MultiAddress;
import com.method5.jot.util.UnitConverter;

import java.math.BigDecimal;
import java.math.BigInteger;

public class BalancesPallet {
    public static byte[] transferKeepAlive(CallIndexResolver resolver, AccountId accountId, BigDecimal amount) {
        return buildTransfer(resolver.resolveCallIndex("Balances", "transfer_keep_alive"), accountId, UnitConverter.toPlanck(amount));
    }

    public static byte[] transferAllowDeath(CallIndexResolver resolver, AccountId accountId, BigDecimal amount) {
        return buildTransfer(resolver.resolveCallIndex("Balances", "transfer_allow_death"), accountId, UnitConverter.toPlanck(amount));
    }

    private static byte[] buildTransfer(byte[] callIndex, AccountId destination, BigInteger amount) {
        ScaleWriter writer = new ScaleWriter();
        writer.writeBytes(callIndex);
        writer.writeBytes(MultiAddress.fromAccountId(destination).encode());
        writer.writeCompact(amount);
        return writer.toByteArray();
    }

    public static byte[] transferAll(CallIndexResolver resolver, AccountId accountId, boolean keepAlive) {
        ScaleWriter writer = new ScaleWriter();
        writer.writeBytes(resolver.resolveCallIndex("Balances", "transfer_all"));
        writer.writeBytes(MultiAddress.fromAccountId(accountId).encode());
        writer.writeBoolean(keepAlive);
        return writer.toByteArray();
    }
}
