package com.method5.jot.extrinsic;

import com.method5.jot.TestBase;
import com.method5.jot.entity.DispatchError;
import com.method5.jot.entity.Phase;
import com.method5.jot.events.EventRecord;
import com.method5.jot.extrinsic.call.BalancesPallet;
import com.method5.jot.entity.Extrinsic;
import com.method5.jot.entity.Mortality;
import com.method5.jot.query.model.AccountId;
import com.method5.jot.scale.ScaleWriter;
import com.method5.jot.util.HexUtil;
import com.method5.jot.wallet.Wallet;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExtrinsicTest extends TestBase {
    private static final Logger logger = LoggerFactory.getLogger(ExtrinsicTest.class);

    @Test
    public void testSignedEncodeDecodeWithAllDetails() throws Exception {
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

    @Test
    public void testDecodeInvalidVersion() {
        ScaleWriter writer = new ScaleWriter();
        writer.writeCompact(BigInteger.valueOf(99));

        Throwable t = assertThrows(Exception.class, () -> ExtrinsicDecoder.decode(writer.toByteArray(), metadata));
        assertTrue(t.getMessage().contains("version not supported"));
    }

    @Test
    public void testCreateAndUpdateResult() {
        ExtrinsicResult result = new ExtrinsicResult("00", true, null, new ArrayList<>());

        assertEquals("00", result.getHash());
        assertNull(result.getError());
        assertTrue(result.isSuccess());
        assertEquals(0, result.getEvents().size());

        result.setHash("01");
        result.setSuccess(false);
        result.setError(DispatchError.unknown());
        result.setEvents(new ArrayList<>(List.of(new EventRecord(new Phase(Phase.Type.FINALIZATION, 0), "pallet", "method", null))));

        assertEquals("01", result.getHash());
        assertFalse(result.isSuccess());

        logger.info(result.toString());
    }
}
