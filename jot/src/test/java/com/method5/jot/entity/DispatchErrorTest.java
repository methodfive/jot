package com.method5.jot.entity;

import com.method5.jot.TestBase;
import com.method5.jot.metadata.RuntimeTypeDecoder;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class DispatchErrorTest extends TestBase {
    private static final Logger logger = LoggerFactory.getLogger(DispatchErrorTest.class);

    @Test
    void testUnknownError() {
        DispatchError dispatchError = DispatchError.unknown();

        assertFalse(dispatchError.isModuleError());
        assertFalse(dispatchError.isNamedError("test"));
        logger.info(dispatchError.toString());
    }

    @Test
    void testNamed() {
        DispatchError dispatchError = DispatchError.named("test");

        assertFalse(dispatchError.isModuleError());
        assertTrue(dispatchError.isNamedError("test"));
        logger.info(dispatchError.toString());
    }

    @Test
    void testModule() {
        DispatchError dispatchError = DispatchError.module(0, 1, "test");

        assertTrue(dispatchError.isModuleError());
        assertFalse(dispatchError.isNamedError("test"));
        logger.info(dispatchError.toString());
    }

    @Test
    void testDecodeUnknown() {
        DispatchError dispatchError = DispatchError.decode(new RuntimeTypeDecoder.TypeAndValue(0, null), resolver);
        assertTrue(dispatchError.isUnknown());
    }

    @Test
    void testDecodeNotMap() {
        DispatchError dispatchError = DispatchError.decode(new RuntimeTypeDecoder.TypeAndValue(0, "test"), resolver);
        assertTrue(dispatchError.isUnknown());
    }

    @Test
    void testDecodeModule() {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> field0 = new HashMap<>();
        map.put("_variant", "Module");
        map.put("field0", field0);

        DispatchError dispatchError = DispatchError.decode(new RuntimeTypeDecoder.TypeAndValue(0, map), resolver);
        assertTrue(dispatchError.isModuleError());
    }

    @Test
    void testDecodeNamed() {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> field0 = new HashMap<>();
        map.put("_variant", "test");
        map.put("field0", field0);
        field0.put("_variant", "other");

        DispatchError dispatchError = DispatchError.decode(new RuntimeTypeDecoder.TypeAndValue(0, map), resolver);
        assertTrue(dispatchError.isNamedError("test.other"));
    }

    @Test
    void testDecodeElse() {
        Map<String, Object> map = new HashMap<>();
        map.put("_variant", "test");

        DispatchError dispatchError = DispatchError.decode(new RuntimeTypeDecoder.TypeAndValue(0, map), resolver);
        assertTrue(dispatchError.isNamedError("test"));
    }
}
