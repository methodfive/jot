package com.method5.jot.query;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.method5.jot.TestBase;
import com.method5.jot.rpc.PolkadotWs;
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
        PolkadotWs api = mock(PolkadotWs.class);

        byte[] extrinsic = new byte[1924];

        ArrayNode params = mapper.createArrayNode();
        params.add(HexUtil.bytesToHex(extrinsic));

        when(api.send("author_submitExtrinsic", params)).thenReturn(new TextNode("00"));
        when(api.getChainSpec()).thenReturn(chainSpec);
        when(api.query()).thenReturn(new Query(api));

        String extrinsicHash = api.query().author().submitExtrinsic(extrinsic);

        assertNotNull(extrinsicHash);
        assertEquals("00", extrinsicHash);
    }
}
