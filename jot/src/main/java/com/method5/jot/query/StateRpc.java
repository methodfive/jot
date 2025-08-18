package com.method5.jot.query;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.method5.jot.query.model.RuntimeVersion;
import com.method5.jot.rpc.PolkadotClient;
import com.method5.jot.util.HexUtil;

public class StateRpc {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static byte[] metadata(PolkadotClient client) throws Exception {
        return HexUtil.hexToBytes(client.send("state_getMetadata", mapper.createArrayNode()).asText());
    }

    public static byte[] storage(PolkadotClient client, String storageKey) throws Exception {
        return storage(client, storageKey, null);
    }

    public static byte[] storage(PolkadotClient client, String storageKey, String blockHash) throws Exception {
        ArrayNode params = mapper.createArrayNode();
        params.add(storageKey);
        if(blockHash != null) {
            params.add(blockHash);
        }
        JsonNode result = client.send("state_getStorage", params);
        return HexUtil.hexToBytes(result.asText());
    }

    public static RuntimeVersion runtimeVersion(PolkadotClient client) throws Exception {
        JsonNode result = client.send("state_getRuntimeVersion", mapper.createArrayNode());
        return mapper.treeToValue(result, RuntimeVersion.class);
    }
}
