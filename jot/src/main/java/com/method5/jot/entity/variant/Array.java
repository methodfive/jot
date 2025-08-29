package com.method5.jot.entity.variant;

import com.method5.jot.entity.VariantType;
import com.method5.jot.entity.metadata.MetadataTypeDefinition;
import com.method5.jot.scale.ScaleWriter;

import java.math.BigInteger;

/**
 * Array — class for array in the Jot SDK. Provides types and data models.
 */
public class Array extends MetadataTypeDefinition {
    private int length;
    private int type;

    public Array(int length, int type) {
        super();
        this.length = length;
        this.type = type;
    }

    @Override
    public byte[] encode() {
        ScaleWriter writer = new ScaleWriter();
        writer.writeByte(VariantType.ARRAY.getType());
        writer.writeInt(length);
        writer.writeCompact(BigInteger.valueOf(type));
        return writer.toByteArray();
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Array{" +
                "length=" + length +
                ", type=" + type +
                '}';
    }
}
