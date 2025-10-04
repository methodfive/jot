package com.method5.jot.entity;

import com.method5.jot.scale.ScaleReader;

/**
 * Mortality — class for mortality in the Jot SDK. Provides key management and signing; types and
 * data models.
 */
public class Mortality {
    private final boolean isImmortal;
    private final int period;
    private final int phase;

    private Mortality(boolean isImmortal, int period, int phase) {
        this.isImmortal = isImmortal;
        this.period = period;
        this.phase = phase;
    }

    public static Mortality mortal(int blockNumber) {
        int period = 64;
        int phase = blockNumber % period;
        return new Mortality(false, period, phase);
    }

    public static Mortality immortal() {
        return new Mortality(true, 0, 0);
    }

    public static Mortality decode(ScaleReader reader) {
        byte first = reader.readByte();
        if (first == 0x00) {
            return Mortality.immortal();
        } else {
            byte second = reader.readByte();

            int encoded = (Byte.toUnsignedInt(second) << 8) | Byte.toUnsignedInt(first);

            // Step 1: Extract period exponent (low 4 bits)
            int periodExp = encoded & 0x0F;
            int period = 2 << periodExp;

            // Step 2: Extract phase
            int quantizeFactor = Math.max(period >> 12, 1);
            int phase = (encoded >> 4) * quantizeFactor;

            return new Mortality(false, period, phase);
        }
    }

    public byte[] encode() {
        if (isImmortal) {
            return new byte[] { 0x00 };
        }

        int trailing = Integer.numberOfTrailingZeros(period);
        int quantizeFactor = Math.max(period >> 12, 1);
        int encodedPhase = phase / quantizeFactor;

        int encoded = (encodedPhase << 4) | (trailing - 1);

        return new byte[] {
                (byte)(encoded & 0xFF),
                (byte)((encoded >> 8) & 0xFF)
        };
    }

    public boolean isImmortal() {
        return isImmortal;
    }

    public int getPeriod() {
        return period;
    }

    public int getPhase() {
        return phase;
    }
}

