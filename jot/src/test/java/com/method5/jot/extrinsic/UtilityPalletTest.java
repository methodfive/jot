package com.method5.jot.extrinsic;

import com.method5.jot.TestBase;
import com.method5.jot.query.model.AccountId;
import com.method5.jot.util.HexUtil;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UtilityPalletTest extends TestBase {
    @Test
    public void testBatch() {
        byte[] callData = api.tx().utility().batch(
                List.of(api.tx().balances().transferAllowDeath(
                        AccountId.fromSS58("13UVJyLnbVp9RBZYFwFGyDvVd1y27Tt8tkntv6Q7JVPhFsTB"),
                        new BigDecimal(1)))).callData();
        assertNotNull(callData);
        assertEquals("1a00040500006d6f646c70792f747273727900000000000000000000000000000000000000000700e40b5402", HexUtil.bytesToHex(callData));
    }

    @Test
    public void testBatchAll() {
        byte[] callData = api.tx().utility().batchAll(
                List.of(api.tx().balances().transferAllowDeath(
                        AccountId.fromSS58("13UVJyLnbVp9RBZYFwFGyDvVd1y27Tt8tkntv6Q7JVPhFsTB"),
                        new BigDecimal(1)))).callData();
        assertNotNull(callData);
        assertEquals("1a02040500006d6f646c70792f747273727900000000000000000000000000000000000000000700e40b5402", HexUtil.bytesToHex(callData));
    }

    @Test
    public void testForceBatch() {
        byte[] callData = api.tx().utility().forceBatch(
                List.of(api.tx().balances().transferAllowDeath(
                        AccountId.fromSS58("13UVJyLnbVp9RBZYFwFGyDvVd1y27Tt8tkntv6Q7JVPhFsTB"),
                        new BigDecimal(1)))).callData();
        assertNotNull(callData);
        assertEquals("1a04040500006d6f646c70792f747273727900000000000000000000000000000000000000000700e40b5402", HexUtil.bytesToHex(callData));
    }
}
