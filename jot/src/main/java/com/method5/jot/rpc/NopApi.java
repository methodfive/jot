package com.method5.jot.rpc;

import com.fasterxml.jackson.databind.JsonNode;
import com.method5.jot.metadata.CallIndexResolver;
import com.method5.jot.query.Query;

/**
 * Nop Client — class for generating call data in the Jot SDK.
 * integration.
 */
public class NopApi extends Api {
    public NopApi(CallIndexResolver resolver) {
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
}
