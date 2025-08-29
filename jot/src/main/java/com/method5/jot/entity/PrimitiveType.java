package com.method5.jot.entity;

/**
 * PrimitiveType — enum for primitive type in the Jot SDK. Provides types and data models.
 */
public enum PrimitiveType {
    BOOLEAN(0),
    CHAR(1),
    STRING(2),
    U8(3),
    U16(4),
    U32(5),
    U64(6),
    U128(7),
    U256(8),
    I8(9),
    I16(10),
    I32(11),
    I64(12),
    I128(13),
    I256(14);

    private int type;

    PrimitiveType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static PrimitiveType fromId(int id) {
        for (PrimitiveType vt : values()) {
            if (vt.type == id) {
                return vt;
            }
        }
        throw new IllegalArgumentException("Unknown PrimitiveType id: " + id);
    }
}
