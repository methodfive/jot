package com.method5.jot.extrinsic.call;

import com.method5.jot.metadata.RuntimeTypeDecoder;

import java.util.Map;

public interface EventClass<T> {
    // Factory method that must create a new instance
    static <T> T create(Map<String, RuntimeTypeDecoder.TypeAndValue> attributes) {
        throw new UnsupportedOperationException("Must be implemented by subclass");
    }
}