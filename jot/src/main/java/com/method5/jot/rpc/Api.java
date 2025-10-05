package com.method5.jot.rpc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.method5.jot.entity.metadata.MetadataV14;
import com.method5.jot.extrinsic.call.Transaction;
import com.method5.jot.metadata.CallIndexResolver;
import com.method5.jot.metadata.MetadataCache;
import com.method5.jot.metadata.MetadataParser;
import com.method5.jot.query.Query;
import com.method5.jot.query.model.RuntimeVersion;
import com.method5.jot.spec.ChainSpec;
import com.method5.jot.spec.ChainSpecBuilder;
import com.method5.jot.util.HexUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * PolkadotClient — class for polkadot client in the Jot SDK. Provides RPC client / JSON‑RPC
 * integration.
 */
public abstract class Api implements AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(Api.class);

    static final ObjectMapper objectMapper = new ObjectMapper();

    protected boolean isInitialized = false;
    protected ChainSpec chainSpec;
    protected CallIndexResolver resolver;
    protected MetadataV14 metadata;

    protected Query query;
    protected Transaction tx;

    protected final String[] servers;
    protected final AtomicInteger currentIndex = new AtomicInteger(0);
    final long timeoutInMillis;

    protected Api() {
        this.servers = null;
        this.timeoutInMillis = 0L;
        this.tx = new Transaction(this);
        this.query = new Query(this);
    }

    protected Api(String[] servers, long timeoutInMillis) {
        this.servers = servers;
        this.timeoutInMillis = timeoutInMillis;
        this.tx = new Transaction(this);
        this.query = new Query(this);

        if (servers == null || servers.length == 0) {
            throw new IllegalArgumentException("At least one RPC URL is required");
        }
    }

    public abstract JsonNode send(String method, JsonNode params) throws Exception;

    public void initializeMetadata() {
        if (isInitialized) return;

        try {
            String genesisHash = query().chain().genesisBlockHash();
            RuntimeVersion runtimeVersion = query().state().runtimeVersion();
            String cacheKey = MetadataCache.key(genesisHash, runtimeVersion.getSpecVersion(), runtimeVersion.getTransactionVersion());

            MetadataCache.CachedBundle hit = MetadataCache.get(cacheKey);
            if (hit != null) {
                this.chainSpec = hit.chainSpec;

                CallIndexResolver r = new CallIndexResolver();
                MetadataParser parser = new MetadataParser(r);

                MetadataV14 md = (hit.metadata != null) ? hit.metadata : parser.parse(HexUtil.hexToBytes(hit.metadataHex));
                if (hit.metadata == null) {
                    hit.metadata = md;
                }

                this.metadata = md;
                this.resolver = r;
                this.isInitialized = true;
                logger.debug("Loaded metadata from cache: {}", cacheKey);
                return;
            }

            ChainSpecBuilder chainSpecBuilder = new ChainSpecBuilder(this);
            ChainSpec cs = chainSpecBuilder.build();

            CallIndexResolver r = new CallIndexResolver();
            MetadataParser parser = new MetadataParser(r);
            MetadataV14 md = parser.parse(cs.getMetadata());

            MetadataCache.put(cacheKey, cs, md, HexUtil.bytesToHex(cs.getMetadata()));

            this.chainSpec = cs;
            this.metadata  = md;
            this.resolver  = r;
            this.isInitialized = true;
            logger.debug("Cached metadata bundle: {}", cacheKey);
        } catch (Exception e) {
            logger.warn("Metadata cache path failed; falling back. {}", e.toString());

            ChainSpecBuilder chainSpecBuilder = new ChainSpecBuilder(this);
            chainSpec = chainSpecBuilder.build();

            resolver = new CallIndexResolver();
            MetadataParser parser = new MetadataParser(resolver);
            metadata = parser.parse(chainSpec.getMetadata());
            isInitialized = true;
        }
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

    public Query query() {
        return query;
    }

    public Transaction tx() {
        return tx;
    }
}
