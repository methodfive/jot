package com.method5.jot.rpc;

import com.fasterxml.jackson.databind.JsonNode;
import com.method5.jot.metadata.CallIndexResolver;
import com.method5.jot.metadata.MetadataCache;
import com.method5.jot.metadata.MetadataParser;
import com.method5.jot.query.Query;
import com.method5.jot.util.HexUtil;

/**
 * Offline Client — class for generating call data in the Jot SDK.
 * integration.
 */
public class OfflineApi extends Api {
    private String genesisHash;
    private long specVersion;
    private long txVersion;

    public OfflineApi() {
        MetadataCache.CachedBundle cachedBundle = MetadataCache.getLatest();
        if(cachedBundle != null) {
            CallIndexResolver r = new CallIndexResolver();
            MetadataParser parser = new MetadataParser(r);
            parser.parse(HexUtil.hexToBytes(cachedBundle.metadataHex));
            this.resolver = r;
            this.genesisHash = cachedBundle.genesisHash;
            this.specVersion = cachedBundle.specVersion;
            this.txVersion = cachedBundle.txVersion;
            this.isInitialized = true;
        } else {
            throw new UnsupportedOperationException("Metadata not initialized");
        }
    }
    public OfflineApi(CallIndexResolver resolver) {
        super();
        this.resolver = resolver;
        this.isInitialized = true;
    }

    @Override
    public JsonNode send(String method, JsonNode params) {
        throw new UnsupportedOperationException("Not supported for NopApi");
    }

    @Override
    public void close() {
    }

    @Override
    public Query query() {
        throw new UnsupportedOperationException("Not supported for NopApi");
    }

    public String getGenesisHash() {
        return genesisHash;
    }

    public long getSpecVersion() {
        return specVersion;
    }

    public long getTxVersion() {
        return txVersion;
    }
}
