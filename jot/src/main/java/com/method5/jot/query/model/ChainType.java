package com.method5.jot.query.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ChainType {
    DEVELOPMENT("Development"),
    LOCAL("Local"),
    LIVE("Live");

    private final String value;

    ChainType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static ChainType fromValue(String value) {
        for (ChainType type : ChainType.values()) {
            if (type.value.equalsIgnoreCase(value)) return type;
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