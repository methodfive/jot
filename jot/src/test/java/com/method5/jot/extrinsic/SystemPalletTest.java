package com.method5.jot.extrinsic;

import com.method5.jot.TestBase;
import com.method5.jot.util.HexUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SystemPalletTest extends TestBase {
    @Test
    public void testRemark() {
        byte[] callData = api.tx().system().remark(
                "test").callData();
        assertNotNull(callData);
        assertEquals("00001074657374", HexUtil.bytesToHex(callData));
    }
}
