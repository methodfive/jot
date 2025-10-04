package com.method5.jot.extrinsic;

import com.method5.jot.TestBase;
import com.method5.jot.extrinsic.call.BalancesPallet;
import com.method5.jot.query.model.AccountId;
import com.method5.jot.util.HexUtil;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BalancesPalletTest extends TestBase {
    @Test
    public void testBalancesTransferAll() {
        byte[] callData = BalancesPallet.transferAll(resolver,
                AccountId.fromSS58("13UVJyLnbVp9RBZYFwFGyDvVd1y27Tt8tkntv6Q7JVPhFsTB"),
                true);
        assertNotNull(callData);
        assertEquals("0504006d6f646c70792f7472737279000000000000000000000000000000000000000001", HexUtil.bytesToHex(callData));
    }

    @Test
    public void testBalancesTransferAllowDeath() {
        byte[] callData = BalancesPallet.transferAllowDeath(resolver,
                AccountId.fromSS58("13UVJyLnbVp9RBZYFwFGyDvVd1y27Tt8tkntv6Q7JVPhFsTB"),
                new BigDecimal(1));
        assertNotNull(callData);
        assertEquals("0500006d6f646c70792f747273727900000000000000000000000000000000000000000700e40b5402", HexUtil.bytesToHex(callData));
    }

    @Test
    public void testBalancesTransferKeepAlive() {
        byte[] callData = BalancesPallet.transferKeepAlive(resolver,
                AccountId.fromSS58("13UVJyLnbVp9RBZYFwFGyDvVd1y27Tt8tkntv6Q7JVPhFsTB"),
                new BigDecimal(1));
        assertNotNull(callData);
        assertEquals("0503006d6f646c70792f747273727900000000000000000000000000000000000000000700e40b5402", HexUtil.bytesToHex(callData));
    }
}
