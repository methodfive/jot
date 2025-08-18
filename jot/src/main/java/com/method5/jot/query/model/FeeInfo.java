package com.method5.jot.query.model;

import com.method5.jot.entity.Weight;

import java.math.BigInteger;

public class FeeInfo {
    private Weight weight;
    private BigInteger partialFee;

    public FeeInfo(Weight weight, BigInteger partialFee) {
        this.weight = weight;
        this.partialFee = partialFee;
    }

    public Weight getWeight() {
        return weight;
    }

    public void setWeight(Weight weight) {
        this.weight = weight;
    }

    public BigInteger getPartialFee() {
        return partialFee;
    }

    public void setPartialFee(BigInteger partialFee) {
        this.partialFee = partialFee;
    }

    @Override
    public String toString() {
        return "FeeInfo{" +
                "weight=" + weight +
                ", partialFee=" + partialFee +
                '}';
    }
}
