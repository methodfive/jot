package com.method5.jot.entity.variant;

import com.method5.jot.entity.VariantType;
import com.method5.jot.entity.metadata.MetadataTypeDefinition;
import com.method5.jot.entity.metadata.MetadataTypeField;
import com.method5.jot.scale.ScaleWriter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Composite — class for composite in the Jot SDK. Provides types and data models.
 */
public class Composite extends MetadataTypeDefinition {
    private List<MetadataTypeField> fields;

    public Composite() {
        fields = new ArrayList<>();
    }

    public Composite(List<MetadataTypeField> fields) {
        super();
        this.fields = fields;
    }

    @Override
    public byte[] encode() {
        ScaleWriter writer = new ScaleWriter();
        writer.writeByte(VariantType.COMPOSITE.getType());
        encodeFields(writer, fields);
        return writer.toByteArray();
    }

    public static void encodeFields(ScaleWriter writer, List<MetadataTypeField> fields) {
        writer.writeCompact(BigInteger.valueOf(fields.size()));
        for (MetadataTypeField field : fields) {
            writer.writeBoolean(field.getName() != null);
            if (field.getName() != null) {
                writer.writeString(field.getName());
            }
            writer.writeCompact(BigInteger.valueOf(field.getType()));
            writer.writeBoolean(field.getTypeName() != null);
            if (field.getTypeName() != null) {
                writer.writeString(field.getTypeName());
            }
            writer.writeStringList(field.getDocs());
        }
    }

    public List<MetadataTypeField> getFields() {
        return fields;
    }

    public void setFields(List<MetadataTypeField> fields) {
        this.fields = fields;
    }

    @Override
    public String toString() {
        return "Composite{" +
                "fields=" + fields +
                '}';
    }
}
