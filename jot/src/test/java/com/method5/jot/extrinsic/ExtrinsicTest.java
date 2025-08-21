package com.method5.jot.extrinsic;

import com.method5.jot.TestBase;
import com.method5.jot.extrinsic.call.BalancesPallet;
import com.method5.jot.entity.Extrinsic;
import com.method5.jot.entity.Mortality;
import com.method5.jot.query.model.AccountId;
import com.method5.jot.util.HexUtil;
import com.method5.jot.wallet.Wallet;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

public class ExtrinsicTest extends TestBase {
    @Test
    public void testSignedEncodeDecode() throws Exception {
        Wallet wallet = Wallet.generate();

        byte[] callData = BalancesPallet.transferKeepAlive(
                resolver,
                AccountId.fromSS58("13NHcoGFJsHJoCYVsJrrv2ygLtz2XJSR17KrnA9QTNYz3Zkz"),
                new BigDecimal("0.001"));

        assertNotNull(callData);

        assertEquals("05030068b48f12d74877afb8c3a3db239fa11179ab344e071b8a53a9ca2e865b29d026025a6202", HexUtil.bytesToHex(callData));

        byte[] extrinsicBytes = ExtrinsicSigner.signAndBuild(callData,
                BigInteger.ONE,
                BigInteger.TWO,
                wallet.getSigner(),
                Mortality.immortal(),
                4,
                10,
                new byte[32],
                new byte[32]);

        assertNotNull(extrinsicBytes);

        Extrinsic extrinsic = ExtrinsicDecoder.decode(extrinsicBytes, metadata);

        assertTrue(extrinsic.isSigned());
        assertEquals(HexUtil.bytesToHex(wallet.getSigner().getPublicKey()), extrinsic.getSigner().getAddress());
        assertEquals(BigInteger.ONE, extrinsic.getNonce());
        assertEquals(BigInteger.TWO, extrinsic.getTip());
        assertEquals("Balances", extrinsic.getModule());
        assertEquals("transfer_keep_alive", extrinsic.getFunction());
        assertTrue(extrinsic.getMortality().isImmortal());

        assertTrue(wallet.verify(
                ExtrinsicSigner.generateSigningPayload(false, callData, BigInteger.ONE, BigInteger.TWO, Mortality.immortal(), 4, 10, new byte[32], new byte[32]),
                HexUtil.hexToBytes(extrinsic.getSignature().getSignature())));
    }
}
