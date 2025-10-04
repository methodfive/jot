package com.method5.jot.extrinsic;

import com.method5.jot.TestBase;
import com.method5.jot.extrinsic.call.SystemPallet;
import com.method5.jot.util.HexUtil;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SystemPalletTest extends TestBase {
    @Test
    public void testRemark() {
        byte[] callData = SystemPallet.remark(resolver,
                "test".getBytes(StandardCharsets.UTF_8));
        assertNotNull(callData);
        assertEquals("00001074657374", HexUtil.bytesToHex(callData));
    }
}
