package com.method5.jot.entity;

import com.method5.jot.scale.ScaleReader;
import com.method5.jot.scale.ScaleWriter;

import java.math.BigInteger;

/**
 * Weight — record for weight in the Jot SDK. Provides types and data models.
 */
public record Weight(BigInteger refTime, BigInteger proofSize) {
    public byte[] encode() {
        ScaleWriter writer = new ScaleWriter();
        writer.writeCompact(refTime);
        writer.writeCompact(proofSize);
        return writer.toByteArray();
    }

    public static Weight decode(ScaleReader reader) {
        BigInteger refTime = reader.readCompact();
        BigInteger proofSize = reader.readCompact();
        return new Weight(refTime, proofSize);
    }

    @Override
    public String toString() {
        return "Weight{" +
                "refTime=" + refTime +
                ", proofSize=" + proofSize +
                '}';
    }
}

