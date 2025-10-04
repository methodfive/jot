package com.method5.jot.query;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.method5.jot.TestBase;
import com.method5.jot.rpc.PolkadotWsClient;
import com.method5.jot.util.HexUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthorRpcTest extends TestBase {
    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testSubmitExtrinsic() throws Exception {
        PolkadotWsClient client = mock(PolkadotWsClient.class);

        byte[] extrinsic = new byte[1924];

        ArrayNode params = mapper.createArrayNode();
        params.add(HexUtil.bytesToHex(extrinsic));

        when(client.send("author_submitExtrinsic", params)).thenReturn(new TextNode("00"));
        when(client.getChainSpec()).thenReturn(chainSpec);

        String extrinsicHash = AuthorRpc.submitExtrinsic(client, extrinsic);

        assertNotNull(extrinsicHash);
        assertEquals("00", extrinsicHash);
    }
}
