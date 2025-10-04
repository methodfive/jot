package com.method5.jot.extrinsic;

import com.method5.jot.TestBase;
import com.method5.jot.entity.Timepoint;
import com.method5.jot.entity.Weight;
import com.method5.jot.extrinsic.call.MultisigPallet;
import com.method5.jot.query.model.AccountId;
import com.method5.jot.util.HexUtil;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.method5.jot.extrinsic.call.SystemPallet;

public class MultisigPalletTest extends TestBase {
    @Test
    public void testApproveAsMulti() {
        List<AccountId> signers = Arrays.asList(
                AccountId.fromSS58("13NHcoGFJsHJoCYVsJrrv2ygLtz2XJSR17KrnA9QTNYz3Zkz"),
                AccountId.fromSS58("12T8sPbDjkP3MafjbKJhZAzHGXMPNDEMqc6H96bvfi5AJGkG")
        );

        byte[] callData = MultisigPallet.approveAsMulti(resolver,
                1,
                signers,
                new Timepoint(2, 4),
                SystemPallet.remark(resolver, "test".getBytes(StandardCharsets.UTF_8)),
                new Weight(BigInteger.ONE, BigInteger.TWO)
        );
        assertNotNull(callData);
        assertEquals("1e02010008402b1a479c014e80596569cbe5aa0eda7fe41d84640141c8024e57bba8082d3068b48f12d74877afb8c3a3db239fa11179ab344e071b8a53a9ca2e865b29d026010200000004000000a73cb6c99c0dbf60fb5f4315e12128883730930c7483e2a7cf4b3f46d96d2a1a0408", HexUtil.bytesToHex(callData));
    }

    @Test
    public void testAsMulti() {
        List<AccountId> signers = Arrays.asList(
                AccountId.fromSS58("13NHcoGFJsHJoCYVsJrrv2ygLtz2XJSR17KrnA9QTNYz3Zkz"),
                AccountId.fromSS58("12T8sPbDjkP3MafjbKJhZAzHGXMPNDEMqc6H96bvfi5AJGkG")
        );

        byte[] callData = MultisigPallet.asMulti(resolver,
                2,
                signers,
                null,
                SystemPallet.remark(resolver, "test".getBytes(StandardCharsets.UTF_8)),
                new Weight(BigInteger.ZERO, BigInteger.ZERO));
        assertNotNull(callData);
        assertEquals("1e01020008402b1a479c014e80596569cbe5aa0eda7fe41d84640141c8024e57bba8082d3068b48f12d74877afb8c3a3db239fa11179ab344e071b8a53a9ca2e865b29d02600000010746573740000", HexUtil.bytesToHex(callData));
    }
}
