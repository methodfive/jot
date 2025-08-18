package com.method5.jot.events;

import com.method5.jot.entity.Phase;
import com.method5.jot.metadata.RuntimeTypeDecoder;

import java.util.Map;

public record EventRecord(Phase phase, String pallet, String method,
                          Map<String, RuntimeTypeDecoder.TypeAndValue> attributes) {

    @Override
    public String toString() {
        return "EventRecord{" +
                "pallet='" + pallet + '\'' +
                ", method='" + method + '\'' +
                ", attributes=" + attributes +
                '}';
    }
}
