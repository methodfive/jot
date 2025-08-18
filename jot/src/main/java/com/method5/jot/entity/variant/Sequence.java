package com.method5.jot.entity.variant;

import com.method5.jot.entity.VariantType;
import com.method5.jot.entity.metadata.MetadataTypeDefinition;
import com.method5.jot.scale.ScaleWriter;

import java.math.BigInteger;

public class Sequence extends MetadataTypeDefinition {
    private int type;

    public Sequence(int type) {
        super();
        this.type = type;
    }

    @Override
    public byte[] encode() {
        ScaleWriter writer = new ScaleWriter();
        writer.writeByte(VariantType.SEQUENCE.getType());
        writer.writeCompact(BigInteger.valueOf(type));
        return writer.toByteArray();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Sequence{" +
                "type=" + type +
                '}';
    }
}
