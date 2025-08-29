package com.method5.jot.entity.metadata;

import com.method5.jot.entity.VariantType;
import com.method5.jot.entity.variant.Composite;
import com.method5.jot.scale.ScaleReader;
import com.method5.jot.scale.ScaleWriter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * MetadataTypeVariants — class for metadata type variants in the Jot SDK. Provides runtime
 * metadata decoding; types and data models.
 */
public class MetadataTypeVariants extends MetadataTypeDefinition {
    private List<Variant> variants;

    public MetadataTypeVariants() {
        this.variants = new ArrayList<>();
    }

    public MetadataTypeVariants(List<Variant> variants) {
        super();
        this.variants = variants;
    }

    @Override
    public byte[] encode() {
        ScaleWriter writer = new ScaleWriter();
        writer.writeByte(VariantType.VARIANT.getType());
        writer.writeCompact(BigInteger.valueOf(variants.size()));
        for (Variant v : variants) {
            writer.writeBytes(v.encode());
        }
        return writer.toByteArray();
    }

    public static class Variant {
        private String name;
        private List<MetadataTypeField> fields;
        private int index;
        private List<String> docs;

        public Variant() {
            fields = new ArrayList<>();
            docs = new ArrayList<>();
        }

        public Variant(String name, int index, List<MetadataTypeField> fields) {
            this.name = name;
            this.index = index;
            this.fields = fields;
        }

        public static MetadataTypeVariants.Variant decode(ScaleReader reader) {
            Variant variant = new Variant();
            variant.setName(reader.readString());

            int fieldCount = reader.readCompact().intValue();
            parseField(reader, fieldCount, variant.fields);
            variant.setIndex(reader.readByte());
            variant.setDocs(reader.readStringList());
            return variant;
        }

        public byte[] encode() {
            ScaleWriter writer = new ScaleWriter();
            writer.writeString(name);
            Composite.encodeFields(writer, fields);
            writer.writeByte(index);
            writer.writeStringList(docs);
            return writer.toByteArray();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<MetadataTypeField> getFields() {
            return fields;
        }

        public void setFields(List<MetadataTypeField> fields) {
            this.fields = fields;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public List<String> getDocs() {
            return docs;
        }

        public void setDocs(List<String> docs) {
            this.docs = docs;
        }

        @Override
        public String toString() {
            return "Variant{" +
                    "name='" + name + '\'' +
                    ", fields=" + fields +
                    ", index=" + index +
                    ", docs=" + docs +
                    '}';
        }
    }

    public List<Variant> getVariants() {
        return variants;
    }

    public void setVariants(List<Variant> variants) {
        this.variants = variants;
    }

    @Override
    public String toString() {
        return "MetadataTypeVariants{" +
                "variants=" + variants +
                '}';
    }
}
