package com.method5.jot.metadata;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CallIndexResolverTest {
    @Test
    void testRegisterAndResolveCall() {
        CallIndexResolver resolver = new CallIndexResolver();
        byte[] index = new byte[]{5, 10};
        resolver.registerCall("Balances", "transfer", index);

        assertArrayEquals(index, resolver.resolveCallIndex("Balances", "transfer"));
        assertEquals("Balances", resolver.getModuleName(5));
        assertEquals("transfer", resolver.getFunctionName("Balances", 0));
    }

    @Test
    void testRegisterAndResolveError() {
        CallIndexResolver resolver = new CallIndexResolver();
        resolver.registerCall("Assets", "freeze", new byte[]{3, 1});
        resolver.registerError("Assets", 4, "NoPermission");

        assertEquals("Assets", resolver.getModuleName(3));
        assertEquals("NoPermission", resolver.getModuleError(3, 4));
    }

    @Test
    void testRegisterAndResolveEvent() {
        CallIndexResolver resolver = new CallIndexResolver();
        byte[] index = new byte[]{5, 10};
        resolver.registerCall("Balances", "transfer", index);
        resolver.registerEvent("Balances", "event");

        assertArrayEquals(index, resolver.resolveCallIndex("Balances", "transfer"));
        assertEquals("event", resolver.getEventName(index[0], 0));
        assertEquals("UnknownModule", resolver.getEventName(99, 0));
        assertEquals("UnknownEvent", resolver.getEventName(index[0], 99));
    }

    @Test
    void testUnknownModuleAndErrorHandling() {
        CallIndexResolver resolver = new CallIndexResolver();
        resolver.registerCall("Staking", "bond", new byte[]{2, 0});

        assertNull(resolver.getModuleError(9, 0));
        assertNull(resolver.getModuleName(9));
        assertThrows(IndexOutOfBoundsException.class, () -> resolver.getFunctionName("Staking", 5));
    }

    @Test
    void testClear() {
        CallIndexResolver resolver = new CallIndexResolver();
        resolver.registerCall("System", "remark", new byte[]{0, 0});
        resolver.registerError("System", 1, "TooLong");

        resolver.clear();

        assertNull(resolver.resolveCallIndex("System", "remark"));
        assertNull(resolver.getModuleName(0));
        assertThrows(IndexOutOfBoundsException.class, () -> resolver.getFunctionName("System", 0));
        assertNull(resolver.getModuleError(0, 1));
    }

    @Test
    void testResolveUnknownVariants() {
        CallIndexResolver resolver = new CallIndexResolver();
        assertEquals(0, resolver.resolveVariants(new ArrayList<>(), 99).size());
    }
}
