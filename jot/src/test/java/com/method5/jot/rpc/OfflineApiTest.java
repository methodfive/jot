package com.method5.jot.rpc;

import com.method5.jot.TestBase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class OfflineApiTest extends TestBase {
    @Test
    public void testUnavailableMethods() {
        try (OfflineApi api = new OfflineApi(resolver, "00", 0, 0)) {
            assertThrows(UnsupportedOperationException.class, () -> {
                api.send("test", null);
            });

            assertThrows(UnsupportedOperationException.class, api::query);
        }
    }}
