package com.method5.jot.entity;

import com.method5.jot.metadata.CallIndexResolver;
import com.method5.jot.metadata.RuntimeTypeDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

/**
 * DispatchError — class for dispatch error in the Jot SDK. Provides types and data models.
 */
public class DispatchError {
    private static final Logger logger = LoggerFactory.getLogger(DispatchError.class);
    public enum Kind {
        MODULE,
        NAMED,
        UNKNOWN
    }

    public final Kind kind;
    public final int moduleIndex;
    public final int errorCode;
    public final String name;

    private DispatchError(Kind kind, int moduleIndex, int errorCode, String name) {
        this.kind = kind;
        this.moduleIndex = moduleIndex;
        this.errorCode = errorCode;
        this.name = name;
    }

    public static DispatchError module(int moduleIndex, int errorCode, String name) {
        return new DispatchError(Kind.MODULE, moduleIndex, errorCode, name);
    }

    public static DispatchError named(String name) {
        return new DispatchError(Kind.NAMED, -1, -1, name);
    }

    public static DispatchError unknown() {
        return new DispatchError(Kind.UNKNOWN, -1, -1, "Unknown");
    }

    public static DispatchError unknown(String reason) {
        return new DispatchError(Kind.UNKNOWN, -1, -1, reason);
    }

    @SuppressWarnings("unchecked")
    public static DispatchError decode(RuntimeTypeDecoder.TypeAndValue tv, CallIndexResolver resolver) {
        if (tv == null || tv.getValue() == null) return unknown();

        Object value = tv.getValue();

        if (!(value instanceof Map)) {
            return unknown("Invalid DispatchError structure: not a Map");
        }

        Map<String, Object> outer = (Map<String, Object>) value;
        String variant = (String) outer.get("_variant");

        if ("Module".equals(variant)) {
            Map<String, Object> field0 = (Map<String, Object>) outer.get("field0");

            if (field0 != null) {
                int index;
                int error;
                if(field0.getOrDefault("index", -1) instanceof Byte indexByte) {
                    index = ((Number) indexByte).intValue();
                } else {
                    index = (int) field0.getOrDefault("index", -1);
                }
                //Is there a better way to do this?
                if(field0.get("error") instanceof ArrayList errorList) {
                    error = ((Number) errorList.getFirst()).intValue();
                } else {
                    error = (int) field0.getOrDefault("error", -1);
                }

                String name = resolver != null ? resolver.getModuleError(index, error) : "Unknown module error";
                return module(index, error, name);
            }
        }

        Object inner = outer.get("field0");
        if (inner instanceof @SuppressWarnings("rawtypes")Map innerMap) {
            String innerVariant = (String) innerMap.get("_variant");
            return named(variant + "." + innerVariant);
        }

        return named(variant);
    }

    public boolean isModuleError() {
        return kind == Kind.MODULE;
    }

    public boolean isUnknown() {
        return kind == Kind.UNKNOWN;
    }

    public boolean isNamedError(String expected) {
        return kind == Kind.NAMED && name.equals(expected);
    }

    public String toHuman() {
        return switch (kind) {
            case MODULE -> "Module[" + moduleIndex + "] Error[" + errorCode + "]: " + name;
            case NAMED -> name;
            case UNKNOWN -> "Unknown: " + name;
        };
    }

    @Override
    public String toString() {
        return toHuman();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DispatchError that)) return false;
        return moduleIndex == that.moduleIndex &&
                errorCode == that.errorCode &&
                kind == that.kind &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kind, moduleIndex, errorCode, name);
    }
}
