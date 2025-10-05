package com.method5.jot.extrinsic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BigIntegerNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.method5.jot.TestBase;
import com.method5.jot.entity.Mortality;
import com.method5.jot.query.Query;
import com.method5.jot.query.model.SignedBlock;
import com.method5.jot.rpc.PolkadotWs;
import com.method5.jot.spec.ChainSpec;
import com.method5.jot.util.HexUtil;
import com.method5.jot.wallet.Wallet;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExtrinsicSignerTest extends TestBase {
    private static final ObjectMapper mapper = new ObjectMapper();
    @Test
    public void testSignAndBuild() throws Exception {
        Wallet wallet = Wallet.fromMnemonic("denial cheap device purpose inflict super column inhale quarter candy junk fetch");

        byte[] callData = HexUtil.hexToBytes("05030068b48f12d74877afb8c3a3db239fa11179ab344e071b8a53a9ca2e865b29d026025a6202");

        PolkadotWs client = mock(PolkadotWs.class);
        when(client.send("chain_getFinalizedHead", mapper.createArrayNode())).thenReturn(new TextNode("120c0a19ed212ed7d1309efe01e74691ec66d34819a82e2db3c9253024e7351b"));

        ArrayNode params = mapper.createArrayNode();
        params.add("120c0a19ed212ed7d1309efe01e74691ec66d34819a82e2db3c9253024e7351b");
        when(client.send("chain_getBlock", params)).thenReturn(mapper.convertValue(new SignedBlock(), JsonNode.class));

        ArrayNode params2 = mapper.createArrayNode();
        params2.add("13j68Wqm5CVNAdJi8CRDHZKheXeTxkngzkEUeYzTsJkPApnA");
        when(client.send("system_accountNextIndex", params2)).thenReturn(new BigIntegerNode(BigInteger.TEN));

        when(client.getChainSpec()).thenReturn(new ChainSpec());
        when(client.query()).thenReturn(new Query(client));

        ArrayNode params3 = mapper.createArrayNode();
        String hexBlockNumber = "0x" + Long.toHexString(0);
        params3.add(hexBlockNumber);
        when(client.send("chain_getBlockHash", params3)).thenReturn((new TextNode("120c0a19ed212ed7d1309efe01e74691ec66d34819a82e2db3c9253024e7351b")));

        byte[] extrinsicBytes = ExtrinsicSigner.signAndBuild(client, wallet.getSigner(), callData);

        assertNotNull(extrinsicBytes);

        // result is non-deterministic and can't be compared
    }

    @Test
    public void testGenerateWithLength() {
        byte[] callData = HexUtil.hexToBytes("05030068b48f12d74877afb8c3a3db239fa11179ab344e071b8a53a9ca2e865b29d026025a6202");

        byte[] extrinsicBytes = ExtrinsicSigner.generateSigningPayload(true, callData,
                BigInteger.ONE,
                BigInteger.TWO,
                Mortality.immortal(),
                10,
                1,
                new byte[10], new byte[20]);

        assertNotNull(extrinsicBytes);

        assertEquals("9c05030068b48f12d74877afb8c3a3db239fa11179ab344e071b8a53a9ca2e865b29d026025a6202000408000a0000000100000000000000000000000000000000000000000000000000000000000000000000", HexUtil.bytesToHex(extrinsicBytes));
    }
}
