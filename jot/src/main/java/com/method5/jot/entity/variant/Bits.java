package com.method5.jot.entity.variant;

import com.method5.jot.entity.VariantType;
import com.method5.jot.entity.metadata.MetadataTypeDefinition;
import com.method5.jot.scale.ScaleWriter;

import java.math.BigInteger;

/**
 * Bits — class for bits in the Jot SDK. Provides types and data models.
 */
public class Bits extends MetadataTypeDefinition {
    private int type;
    private int order;

    @Override
    public byte[] encode() {
        ScaleWriter writer = new ScaleWriter();
        writer.writeByte(VariantType.BIT_SEQUENCE.getType());
        writer.writeCompact(BigInteger.valueOf(type));
        writer.writeCompact(BigInteger.valueOf(order));
        return writer.toByteArray();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "Bits{" +
                "type=" + type +
                ", order=" + order +
                '}';
    }
}
