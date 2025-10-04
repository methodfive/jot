package com.method5.jot.query.model;

import com.method5.jot.scale.ScaleReader;
import com.method5.jot.util.UnitConverter;

import java.math.BigDecimal;

/**
 * AccountInfo — class for account info in the Jot SDK. Provides types and data models.
 */
public class AccountInfo {
    private long nonce;
    private long consumers;
    private long providers;
    private long sufficients;

    private BigDecimal free;
    private BigDecimal reserved;
    private BigDecimal frozen;

    public static AccountInfo decode(ScaleReader reader, int decimals) {
        AccountInfo info = new AccountInfo();
        info.nonce = reader.readInt();
        info.consumers = reader.readInt();
        info.providers = reader.readInt();
        info.sufficients = reader.readInt();
        info.free = UnitConverter.fromPlanck(reader.readU128(), decimals);
        info.reserved = UnitConverter.fromPlanck(reader.readU128(), decimals);
        info.frozen = UnitConverter.fromPlanck(reader.readU128(), decimals);
        return info;
    }

    public long getNonce() {
        return nonce;
    }

    public long getConsumers() {
        return consumers;
    }

    public long getProviders() {
        return providers;
    }

    public long getSufficients() {
        return sufficients;
    }

    public BigDecimal getFree() {
        return free;
    }

    public BigDecimal getReserved() {
        return reserved;
    }

    public BigDecimal getFrozen() {
        return frozen;
    }

    @Override
    public String toString() {
        return "AccountInfo{" +
                "nonce=" + nonce +
                ", consumers=" + consumers +
                ", providers=" + providers +
                ", sufficients=" + sufficients +
                ", free=" + free +
                ", reserved=" + reserved +
                ", frozen=" + frozen +
                '}';
    }
}
