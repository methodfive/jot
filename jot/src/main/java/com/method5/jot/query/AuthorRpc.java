package com.method5.jot.query;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.method5.jot.rpc.PolkadotClient;
import com.method5.jot.util.HexUtil;

public class AuthorRpc {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String submitExtrinsic(PolkadotClient client, byte[] extrinsic) throws Exception {
        ArrayNode params = mapper.createArrayNode();
        params.add(HexUtil.bytesToHex(extrinsic));
        JsonNode result = client.send("author_submitExtrinsic", params);
        return result.asText();
    }
}
