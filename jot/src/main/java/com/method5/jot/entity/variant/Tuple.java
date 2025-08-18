package com.method5.jot.entity.variant;

import com.method5.jot.entity.VariantType;
import com.method5.jot.entity.metadata.MetadataTypeDefinition;
import com.method5.jot.scale.ScaleWriter;

import java.math.BigInteger;
import java.util.List;

public class Tuple extends MetadataTypeDefinition {
    private List<Integer> types;

    public Tuple(List<Integer> types) {
        super();
        this.types = types;
    }

    @Override
    public byte[] encode() {
        ScaleWriter writer = new ScaleWriter();
        writer.writeByte(VariantType.TUPLE.getType());
        writer.writeCompact(BigInteger.valueOf(types.size()));
        for (Integer id : types) {
            writer.writeCompact(BigInteger.valueOf(id));
        }
        return writer.toByteArray();
    }

    public List<Integer> getTypes() {
        return types;
    }

    public void setTypes(List<Integer> types) {
        this.types = types;
    }

    @Override
    public String toString() {
        return "Tuple{" +
                ", types=" + types +
                '}';
    }
}
