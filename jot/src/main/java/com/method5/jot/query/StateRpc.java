package com.method5.jot.query;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.method5.jot.query.model.RuntimeVersion;
import com.method5.jot.rpc.Api;
import com.method5.jot.rpc.CallOrQuery;
import com.method5.jot.util.HexUtil;

/**
 * StateRpc — class for state rpc in the Jot SDK. Provides RPC client / JSON‑RPC integration.
 */
public class StateRpc extends CallOrQuery {
    public StateRpc(Api api) {
        super(api);
    }

    public byte[] metadata() throws Exception {
        return HexUtil.hexToBytes(api.send("state_getMetadata", mapper.createArrayNode()).asText());
    }

    public byte[] storage(String storageKey) throws Exception {
        return storage(storageKey, null);
    }

    public byte[] storage(String storageKey, String blockHash) throws Exception {
        ArrayNode params = mapper.createArrayNode();
        params.add(storageKey);
        if(blockHash != null) {
            params.add(blockHash);
        }
        JsonNode result = api.send("state_getStorage", params);
        return result != null && !(result instanceof NullNode) ? HexUtil.hexToBytes(result.asText()) : null;
    }

    public RuntimeVersion runtimeVersion() throws Exception {
        JsonNode result = api.send("state_getRuntimeVersion", mapper.createArrayNode());
        return mapper.treeToValue(result, RuntimeVersion.class);
    }
}
