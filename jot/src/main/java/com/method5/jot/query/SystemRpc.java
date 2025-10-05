package com.method5.jot.query;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.method5.jot.query.model.ChainType;
import com.method5.jot.query.model.SystemHealth;
import com.method5.jot.query.model.SystemProperties;
import com.method5.jot.rpc.Api;
import com.method5.jot.rpc.CallOrQuery;

import java.math.BigInteger;

/**
 * SystemRpc — class for system rpc in the Jot SDK. Provides RPC client / JSON‑RPC integration.
 */
public class SystemRpc extends CallOrQuery {
    public SystemRpc(Api api) {
        super(api);
    }

    public BigInteger accountNextIndex(String address) throws Exception {
        ArrayNode params = mapper.createArrayNode();
        params.add(address);

        JsonNode result = api.send("system_accountNextIndex", params);
        return result.bigIntegerValue();
    }

    public String version() throws Exception {
        JsonNode result = api.send("system_version", JsonNodeFactory.instance.arrayNode());
        return result.asText();
    }

    public String chain() throws Exception {
        return api.send("system_chain", mapper.createArrayNode()).asText();
    }

    public SystemProperties properties() throws Exception {
        JsonNode result = api.send("system_properties", mapper.createArrayNode());
        return mapper.treeToValue(result, SystemProperties.class);
    }

    public ChainType chainType() throws Exception {
        JsonNode result = api.send("system_chainType", mapper.createArrayNode());
        return mapper.treeToValue(result, ChainType.class);
    }

    public String name() throws Exception {
        return api.send("system_name", mapper.createArrayNode()).asText();
    }

    public SystemHealth health() throws Exception {
        JsonNode raw = api.send("system_health", mapper.createArrayNode());
        return mapper.treeToValue(raw, SystemHealth.class);
    }
}

