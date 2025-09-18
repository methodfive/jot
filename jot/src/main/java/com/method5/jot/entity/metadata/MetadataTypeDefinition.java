package com.method5.jot.entity.metadata;

import com.method5.jot.entity.PrimitiveType;
import com.method5.jot.entity.VariantType;
import com.method5.jot.entity.variant.*;
import com.method5.jot.scale.ScaleReader;

import java.util.ArrayList;
import java.util.List;

/**
 * MetadataTypeDefinition — class for metadata type definition in the Jot SDK. Provides runtime
 * metadata decoding; types and data models.
 */
public abstract class MetadataTypeDefinition {
    public static MetadataTypeDefinition decode(ScaleReader reader) {
        int defIndex = reader.readByte();

        if(defIndex == VariantType.COMPOSITE.getType()) {
            Composite composite = new Composite();
            int fieldCount = reader.readCompact().intValue();
            List<MetadataTypeField> fields = new ArrayList<>();
            parseField(reader, fieldCount, fields);
            composite.setFields(fields);
            return composite;
        } else if(defIndex == VariantType.VARIANT.getType()) {
            int variantCount = reader.readCompact().intValue();
            MetadataTypeVariants variants = new MetadataTypeVariants();
            for (int i = 0; i < variantCount; i++) {
                variants.getVariants().add(MetadataTypeVariants.Variant.decode(reader));
            }
            return variants;
        } else if(defIndex == VariantType.SEQUENCE.getType()) {
            return new Sequence(reader.readCompact().intValue());
        } else if(defIndex == VariantType.ARRAY.getType()) {
            return new Array(reader.readInt(), reader.readCompact().intValue());
        } else if(defIndex == VariantType.TUPLE.getType()) {
            int length = reader.readCompact().intValue();
            List<Integer> types = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                types.add(reader.readCompact().intValue());
            }
            return new Tuple(types);
        } else if(defIndex == VariantType.PRIMITIVE.getType()) {
            return new Primitive(PrimitiveType.fromId(reader.readByte()));
        } else if(defIndex == VariantType.COMPACT.getType()) {
            Compact compact = new Compact();
            compact.setType(reader.readCompact().intValue());
            return compact;
        } else if(defIndex == VariantType.BIT_SEQUENCE.getType()) {
            Bits bits = new Bits();
            bits.setType(reader.readCompact().intValue());
            bits.setOrder(reader.readCompact().intValue());
            return bits;
        }
        throw new RuntimeException("Unknown TypeDef variant: " + defIndex);
    }

    static void parseField(ScaleReader reader, int fieldCount, List<MetadataTypeField> fields) {
        for (int i = 0; i < fieldCount; i++) {
            MetadataTypeField f = new MetadataTypeField();
            if (reader.readBoolean()) {
                f.setName(reader.readString());
            }
            f.setType(reader.readCompact().intValue());

            if (reader.readBoolean()) {
                f.setTypeName(reader.readString());
            }
            f.setDocs(reader.readStringList());
            fields.add(f);
        }
    }

    public abstract byte[] encode();
}
