package com.method5.jot.entity.variant;

import com.method5.jot.entity.PrimitiveType;
import com.method5.jot.entity.VariantType;
import com.method5.jot.entity.metadata.MetadataTypeDefinition;
import com.method5.jot.scale.ScaleWriter;

/**
 * Primitive — class for primitive in the Jot SDK. Provides types and data models.
 */
public class Primitive extends MetadataTypeDefinition {
    private PrimitiveType type;

    public Primitive(PrimitiveType type) {
        super();
        this.type = type;
    }

    @Override
    public byte[] encode() {
        ScaleWriter writer = new ScaleWriter();
        writer.writeByte(VariantType.PRIMITIVE.getType());
        writer.writeByte(type.getType());
        return writer.toByteArray();
    }

    public PrimitiveType getType() {
        return type;
    }

    public void setType(PrimitiveType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Primitive{" +
                "type=" + type +
                '}';
    }
}