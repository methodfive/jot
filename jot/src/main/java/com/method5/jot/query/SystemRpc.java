package com.method5.jot.query;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.method5.jot.query.model.ChainType;
import com.method5.jot.query.model.SystemHealth;
import com.method5.jot.query.model.SystemProperties;
import com.method5.jot.rpc.PolkadotClient;

import java.math.BigInteger;

/**
 * SystemRpc — class for system rpc in the Jot SDK. Provides RPC client / JSON‑RPC integration.
 */
public class SystemRpc {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static BigInteger accountNextIndex(PolkadotClient client, String address) throws Exception {
        ArrayNode params = mapper.createArrayNode();
        params.add(address);

        JsonNode result = client.send("system_accountNextIndex", params);
        return result.bigIntegerValue();
    }

    public static String version(PolkadotClient client) throws Exception {
        JsonNode result = client.send("system_version", JsonNodeFactory.instance.arrayNode());
        return result.asText();
    }

    public static String chain(PolkadotClient client) throws Exception {
        return client.send("system_chain", mapper.createArrayNode()).asText();
    }

    public static SystemProperties properties(PolkadotClient client) throws Exception {
        JsonNode result = client.send("system_properties", mapper.createArrayNode());
        return mapper.treeToValue(result, SystemProperties.class);
    }

    public static ChainType chainType(PolkadotClient client) throws Exception {
        JsonNode result = client.send("system_chainType", mapper.createArrayNode());
        return mapper.treeToValue(result, ChainType.class);
    }

    public static String name(PolkadotClient client) throws Exception {
        return client.send("system_name", mapper.createArrayNode()).asText();
    }

    public static SystemHealth health(PolkadotClient client) throws Exception {
        JsonNode raw = client.send("system_health", mapper.createArrayNode());
        return mapper.treeToValue(raw, SystemHealth.class);
    }
}

