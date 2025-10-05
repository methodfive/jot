package com.method5.jot.spec;

import com.method5.jot.query.model.RuntimeVersion;
import com.method5.jot.query.model.SystemProperties;
import com.method5.jot.rpc.Api;
import com.method5.jot.util.HexUtil;

/**
 * ChainSpecBuilder — class for chain spec builder in the Jot SDK. Provides RPC client / JSON‑RPC
 * integration; extrinsic construction and submission.
 */
public class ChainSpecBuilder {
    private final Api api;

    public ChainSpecBuilder(Api api) {
        this.api = api;
    }

    public ChainSpec build() {
        ChainSpec spec = new ChainSpec();

        try {
            spec.setId(api.query().system().chain());
            spec.setName(api.query().system().name());

            spec.setGenesisHash(HexUtil.hexToBytes(api.query().chain().blockHash(0)));

            SystemProperties systemProperties = api.query().system().properties();
            spec.setSs58Prefix(systemProperties.getSs58Format());
            spec.setTokenDecimals(systemProperties.getTokenDecimals());
            spec.setTokenSymbol(systemProperties.getTokenSymbol());

            RuntimeVersion runtimeVersion = api.query().state().runtimeVersion();
            spec.setTransactionVersion(runtimeVersion.getTransactionVersion());
            spec.setSpecVersion(runtimeVersion.getSpecVersion());

            spec.setMetadata(api.query().state().metadata());
        } catch (Exception e) {
            throw new RuntimeException("Failed to build ChainSpec. " + e.getMessage(), e);
        }
        return spec;
    }
}
