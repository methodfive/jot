package com.method5.jot.entity.metadata;

import com.method5.jot.scale.ScaleReader;
import com.method5.jot.scale.ScaleWriter;

import java.math.BigInteger;

/**
 * MetadataExtrinsic — class for metadata extrinsic in the Jot SDK. Provides extrinsic construction
 * and submission; runtime metadata decoding; types and data models.
 */
public class MetadataExtrinsic {
    private String name;
    private int type;
    private int additional;

    public static MetadataExtrinsic decode(ScaleReader reader) {
        MetadataExtrinsic metadata = new MetadataExtrinsic();
        metadata.setName(reader.readString());
        metadata.setType(reader.readCompact().intValue());
        metadata.setAdditional(reader.readCompact().intValue());
        return metadata;
    }

    public byte[] encode() {
        ScaleWriter writer = new ScaleWriter();
        writer.writeString(getName());
        writer.writeCompact(BigInteger.valueOf(getType()));
        writer.writeCompact(BigInteger.valueOf(getAdditional()));
        return writer.toByteArray();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAdditional() {
        return additional;
    }

    public void setAdditional(int additional) {
        this.additional = additional;
    }

    @Override
    public String toString() {
        return "MetadataExtrinsic{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", additional=" + additional +
                '}';
    }
}
