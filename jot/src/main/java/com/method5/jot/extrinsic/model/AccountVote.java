package com.method5.jot.extrinsic.model;

import com.method5.jot.scale.ScaleWriter;
import com.method5.jot.util.UnitConverter;

import java.math.BigDecimal;

/**
 * AccountVote — class for account vote in the Jot SDK. Provides extrinsic construction and
 * submission; types and data models.
 */
public class AccountVote {
    private AccountVoteType type;

    private boolean aye;
    private Conviction conviction;
    private BigDecimal balance;

    private BigDecimal totalAye;
    private BigDecimal totalNay;
    private BigDecimal totalAbstain;

    private AccountVote(boolean aye, Conviction conviction, BigDecimal balance) {
        this.type = AccountVoteType.STANDARD;
        this.aye = aye;
        this.conviction = conviction;
        this.balance = balance;
    }

    private AccountVote(BigDecimal totalAye, BigDecimal totalNay) {
        this.type = AccountVoteType.SPLIT;
        this.totalAye = totalAye;
        this.totalNay = totalNay;
    }

    private AccountVote(BigDecimal totalAye, BigDecimal totalNay, BigDecimal totalAbstain) {
        this.type = AccountVoteType.SPLIT_ABSTAIN;
        this.totalAye = totalAye;
        this.totalNay = totalNay;
        this.totalAbstain = totalAbstain;
    }

    public static AccountVote standard(boolean aye, Conviction conviction, BigDecimal balance) {
        return new AccountVote(aye, conviction, balance);
    }

    public static AccountVote split(BigDecimal totalAye, BigDecimal totalNay) {
        return new AccountVote(totalAye, totalNay);
    }

    public static AccountVote splitAbstain(BigDecimal totalAye, BigDecimal totalNay, BigDecimal totalAbstain) {
        return new AccountVote(totalAye, totalNay, totalAbstain);
    }

    public boolean isAye() {
        return aye;
    }

    public void setAye(boolean aye) {
        this.aye = aye;
    }

    public Conviction getConviction() {
        return conviction;
    }

    public void setConviction(Conviction conviction) {
        this.conviction = conviction;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public AccountVoteType getType() {
        return type;
    }

    public void setType(AccountVoteType type) {
        this.type = type;
    }

    public BigDecimal getTotalAye() {
        return totalAye;
    }

    public void setTotalAye(BigDecimal totalAye) {
        this.totalAye = totalAye;
    }

    public BigDecimal getTotalNay() {
        return totalNay;
    }

    public void setTotalNay(BigDecimal totalNay) {
        this.totalNay = totalNay;
    }

    public BigDecimal getTotalAbstain() {
        return totalAbstain;
    }

    public void setTotalAbstain(BigDecimal totalAbstain) {
        this.totalAbstain = totalAbstain;
    }

    public byte[] encode() {
        if(type == AccountVoteType.STANDARD) {
            int voteByte = (aye ? 0b10000000 : 0) | (conviction.index() & 0b01111111);
            ScaleWriter writer = new ScaleWriter();
            writer.writeByte(type.getValue());
            writer.writeByte(voteByte);
            writer.writeU128(UnitConverter.toPlanck(balance));
            return writer.toByteArray();
        } else if(type == AccountVoteType.SPLIT) {
            ScaleWriter writer = new ScaleWriter();
            writer.writeByte(type.getValue());
            writer.writeU128(UnitConverter.toPlanck(totalAye));
            writer.writeU128(UnitConverter.toPlanck(totalNay));
            return writer.toByteArray();
        } else {
            ScaleWriter writer = new ScaleWriter();
            writer.writeByte(type.getValue());
            writer.writeU128(UnitConverter.toPlanck(totalAye));
            writer.writeU128(UnitConverter.toPlanck(totalNay));
            writer.writeU128(UnitConverter.toPlanck(totalAbstain));
            return writer.toByteArray();
        }
    }

    @Override
    public String toString() {
        return "AccountVote{" +
                "type=" + type +
                ", aye=" + aye +
                ", conviction=" + conviction +
                ", balance=" + balance +
                ", totalAye=" + totalAye +
                ", totalNay=" + totalNay +
                ", totalAbstain=" + totalAbstain +
                '}';
    }
}

