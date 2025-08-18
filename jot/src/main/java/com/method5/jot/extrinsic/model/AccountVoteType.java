package com.method5.jot.extrinsic.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AccountVoteType {
    STANDARD(0),
    SPLIT(1),
    SPLIT_ABSTAIN(2);

    private final int value;

    AccountVoteType(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return value;
    }

    @JsonCreator
    public static AccountVoteType fromValue(int value) {
        for (AccountVoteType type : AccountVoteType.values()) {
            if (type.getValue() == value) return type;
        }
        return null;
    }

    @Override
    public String toString() {
        return "ChainType{" +
                "value='" + value + '\'' +
                '}';
    }
}