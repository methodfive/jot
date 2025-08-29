package com.method5.jot.rpc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.method5.jot.entity.metadata.MetadataV14;
import com.method5.jot.metadata.CallIndexResolver;
import com.method5.jot.metadata.MetadataParser;
import com.method5.jot.spec.ChainSpec;
import com.method5.jot.spec.ChainSpecBuilder;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * PolkadotClient — class for polkadot client in the Jot SDK. Provides RPC client / JSON‑RPC
 * integration.
 */
public abstract class PolkadotClient implements AutoCloseable {
    static final ObjectMapper objectMapper = new ObjectMapper();

    protected final boolean isInitialized = false;
    protected ChainSpec chainSpec;
    protected CallIndexResolver resolver;
    protected MetadataV14 metadata;

    protected final String[] servers;
    protected final AtomicInteger currentIndex = new AtomicInteger(0);
    final long timeoutInMillis;

    protected PolkadotClient(String[] servers, long timeoutInMillis) {
        this.servers = servers;
        this.timeoutInMillis = timeoutInMillis;

        if (servers == null || servers.length == 0) {
            throw new IllegalArgumentException("At least one RPC URL is required");
        }
    }

    public abstract JsonNode send(String method, JsonNode params) throws Exception;

    public String send(String method, String params) throws Exception {
        return objectMapper.writeValueAsString(send(method, objectMapper.readTree(params)));
    }

    public void initializeMetadata() {
        ChainSpecBuilder chainSpecBuilder = new ChainSpecBuilder(this);
        chainSpec = chainSpecBuilder.build();

        // Parse metadata
        resolver = new CallIndexResolver();
        MetadataParser parser = new MetadataParser(resolver);
        metadata = parser.parse(chainSpec.getMetadata());
    }

    protected String buildPayload(String id, String method, JsonNode params) {
        return objectMapper.createObjectNode()
                .put("jsonrpc", "2.0")
                .put("id", id)
                .put("method", method)
                .set("params", params)
                .toString();
    }

    public abstract void close();

    public ChainSpec getChainSpec() {
        if(!isInitialized) {
            initializeMetadata();
        }
        return chainSpec;
    }

    public CallIndexResolver getResolver() {
        if(!isInitialized) {
            initializeMetadata();
        }
        return resolver;
    }

    public MetadataV14 getMetadata() {
        if(!isInitialized) {
            initializeMetadata();
        }
        return metadata;
    }
}
