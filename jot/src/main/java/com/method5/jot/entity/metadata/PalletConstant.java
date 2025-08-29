package com.method5.jot.entity.metadata;

import com.method5.jot.scale.ScaleReader;
import com.method5.jot.scale.ScaleWriter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * PalletConstant — class for pallet constant in the Jot SDK. Provides runtime metadata decoding;
 * types and data models; pallet call builders.
 */
public class PalletConstant {
    private int type;
    private String name;
    private byte[] value;
    private List<String> docs;

    public PalletConstant() {
        docs = new ArrayList<>();
    }

    public static PalletConstant decode(ScaleReader reader) {
        PalletConstant constant = new PalletConstant();
        constant.setName(reader.readString());
        constant.setType(reader.readCompact().intValue());
        constant.setValue(reader.readCompactBytes());
        constant.setDocs(reader.readStringList());
        return constant;
    }

    public byte[] encode() {
        ScaleWriter scaleWriter = new ScaleWriter();
        scaleWriter.writeString(getName());
        scaleWriter.writeCompact(BigInteger.valueOf(getType()));
        scaleWriter.writeCompactBytes(getValue());
        scaleWriter.writeStringList(getDocs());
        return scaleWriter.toByteArray();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    public List<String> getDocs() {
        return docs;
    }

    public void setDocs(List<String> docs) {
        this.docs = docs;
    }

    @Override
    public String toString() {
        return "PalletConstant{" +
                "type=" + type +
                ", name='" + name + '\'' +
                ", value=" + Arrays.toString(value) +
                ", docs=" + docs +
                '}';
    }
}
