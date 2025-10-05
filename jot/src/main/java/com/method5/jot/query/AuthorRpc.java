package com.method5.jot.query;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.method5.jot.rpc.Api;
import com.method5.jot.rpc.CallOrQuery;
import com.method5.jot.util.HexUtil;

/**
 * AuthorRpc — class for author rpc in the Jot SDK. Provides RPC client / JSON‑RPC integration.
 */
public class AuthorRpc extends CallOrQuery {
    public AuthorRpc(Api api) {
        super(api);
    }

    public String submitExtrinsic(byte[] extrinsic) throws Exception {
        ArrayNode params = mapper.createArrayNode();
        params.add(HexUtil.bytesToHex(extrinsic));
        JsonNode result = api.send("author_submitExtrinsic", params);
        return result.asText();
    }
}
