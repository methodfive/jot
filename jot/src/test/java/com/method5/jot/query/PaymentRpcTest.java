package com.method5.jot.query;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.method5.jot.TestBase;
import com.method5.jot.query.model.FeeInfo;
import com.method5.jot.rpc.PolkadotWsClient;
import com.method5.jot.util.HexUtil;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PaymentRpcTest extends TestBase {
    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testQueryInfo() throws Exception {
        PolkadotWsClient client = mock(PolkadotWsClient.class);

        String extrinsic = "0xbd018400c8bcf52e2155e72204388c9f23b58bf79a60b43f33430bd8b121e05d591af16201545225fa966c59e6ce81c158d25d787a67645614909c1c9afe313c56c8581608ac5e936cf277120c1d313efadf77ee4b2d4373bd954d545d082919cf6de6f680150100000000001074657374";

        ArrayNode params = mapper.createArrayNode();
        params.add(extrinsic);

        ObjectNode expected = (ObjectNode) new ObjectMapper().readTree("{\"weight\":{\"ref_time\":362669958,\"proof_size\":7186},\"class\":\"normal\",\"partialFee\":\"125877305\"}");

        when(client.send("payment_queryInfo", params)).thenReturn(expected);
        when(client.getChainSpec()).thenReturn(chainSpec);

        FeeInfo feeInfo = PaymentRpc.queryInfo(client, extrinsic);

        assertNotNull(feeInfo);
        assertEquals("1a9e77564970", HexUtil.bytesToHex(feeInfo.getWeight().encode()));
        assertEquals(BigInteger.valueOf(125877305), feeInfo.getPartialFee());
    }

    @Test
    public void testQueryInfoAt() throws Exception {
        PolkadotWsClient client = mock(PolkadotWsClient.class);

        String extrinsic = "0xbd018400c8bcf52e2155e72204388c9f23b58bf79a60b43f33430bd8b121e05d591af16201545225fa966c59e6ce81c158d25d787a67645614909c1c9afe313c56c8581608ac5e936cf277120c1d313efadf77ee4b2d4373bd954d545d082919cf6de6f680150100000000001074657374";

        ArrayNode params = mapper.createArrayNode();
        params.add(extrinsic);
        params.add("0x54adc3902b0e959e865b53651353ba655c1a17d465a6691e965bb374d9457df5");

        ObjectNode expected = (ObjectNode) new ObjectMapper().readTree("{\"weight\":{\"ref_time\":362669958,\"proof_size\":7186},\"class\":\"normal\",\"partialFee\":\"125877305\"}");

        when(client.send("payment_queryInfo", params)).thenReturn(expected);
        when(client.getChainSpec()).thenReturn(chainSpec);

        FeeInfo feeInfo = PaymentRpc.queryInfo(client, extrinsic, "0x54adc3902b0e959e865b53651353ba655c1a17d465a6691e965bb374d9457df5");

        assertNotNull(feeInfo);
        assertEquals("1a9e77564970", HexUtil.bytesToHex(feeInfo.getWeight().encode()));
        assertEquals(BigInteger.valueOf(125877305), feeInfo.getPartialFee());
    }
}
