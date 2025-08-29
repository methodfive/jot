package com.method5.jot.entity;

import com.method5.jot.scale.ScaleReader;
import com.method5.jot.scale.ScaleWriter;

/**
 * Timepoint — record for timepoint in the Jot SDK. Provides types and data models.
 */
public record Timepoint(int height, int index) {
    public byte[] encode() {
        ScaleWriter scaleWriter = new ScaleWriter();
        scaleWriter.writeInt(height);
        scaleWriter.writeInt(index);
        return scaleWriter.toByteArray();
    }

    public static Timepoint decode(ScaleReader reader) {
        int height = reader.readInt();
        int index = reader.readInt();
        return new Timepoint(height, index);
    }

    @Override
    public String toString() {
        return "Timepoint{" +
                "height=" + height +
                ", index=" + index +
                '}';
    }
}
