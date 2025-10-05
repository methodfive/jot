package com.method5.jot.rpc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.method5.jot.metadata.CallIndexResolver;

/**
 * CallOrQuery — Base class for all RPC calls and queries in the Jot SDK.
 */
public class CallOrQuery {
    protected final ObjectMapper mapper;
    protected final Api api;

    public CallOrQuery(Api api) {
        this.mapper = new ObjectMapper();
        this.api = api;
    }

    public CallIndexResolver getResolver() {
        return api.getResolver();
    }
}
