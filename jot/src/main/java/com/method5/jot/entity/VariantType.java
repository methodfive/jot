package com.method5.jot.entity;

public enum VariantType {
    COMPOSITE(0, "composite"),
    VARIANT(1, "variant"),
    SEQUENCE(2, "sequence"),
    ARRAY(3, "array"),
    TUPLE(4, "tuple"),
    PRIMITIVE(5, "primitive"),
    COMPACT(6, "compact"),
    BIT_SEQUENCE(7, "bitsequence");

    private final int type;
    private final String name;

    VariantType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public static VariantType fromId(int id) {
        for (VariantType vt : values()) {
            if (vt.type == id) {
                return vt;
            }
        }
        throw new IllegalArgumentException("Unknown VariantType id: " + id);
    }

    @Override
    public String toString() {
        return "VariantType{" +
                "type=" + type +
                ", name='" + name + '\'' +
                '}';
    }
}