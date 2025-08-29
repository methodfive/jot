package com.method5.jot.entity;

import com.method5.jot.scale.ScaleReader;

/**
 * Phase — record for phase in the Jot SDK. Provides key management and signing; types and data
 * models.
 */
public record Phase(com.method5.jot.entity.Phase.Type type, int extrinsicIndex) {
    public enum Type {
        APPLY_EXTRINSIC,
        FINALIZATION,
        INITIALIZATION,
        UNKNOWN
    }

    public boolean isApplyExtrinsic(int index) {
        return type == Type.APPLY_EXTRINSIC && extrinsicIndex == index;
    }

    public static Phase decode(ScaleReader reader) {
        int variant = Byte.toUnsignedInt(reader.readByte());

        return switch (variant) {
            case 0 -> {
                int index = reader.readInt();
                yield new Phase(Type.APPLY_EXTRINSIC, index);
            }
            case 1 -> new Phase(Type.FINALIZATION, -1);
            case 2 -> new Phase(Type.INITIALIZATION, -1);
            default -> new Phase(Type.UNKNOWN, -1);
        };
    }

    @Override
    public String toString() {
        return switch (type) {
            case APPLY_EXTRINSIC -> "ApplyExtrinsic(" + extrinsicIndex + ")";
            case FINALIZATION -> "Finalization";
            case INITIALIZATION -> "Initialization";
            default -> "Unknown";
        };
    }
}
