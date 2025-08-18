package com.method5.jot.extrinsic.model;

public enum Conviction {
    NONE(0),        // 0.1x lock, no additional weight
    LOCKED_1X(1),   // 1x conviction, locked for 1 enactment
    LOCKED_2X(2),   // 2x conviction, locked for 2 enactments
    LOCKED_3X(3),   // 3x conviction, locked for 4 enactments
    LOCKED_4X(4),   // 4x conviction, locked for 8 enactments
    LOCKED_5X(5),   // 5x conviction, locked for 16 enactments
    LOCKED_6X(6);   // 6x conviction, locked for 32 enactments

    private final int index;

    Conviction(int index) {
        this.index = index;
    }

    public int index() {
        return index;
    }
}
