package com.method5.jot.spec;

import com.method5.jot.query.ChainRpc;
import com.method5.jot.query.StateRpc;
import com.method5.jot.query.SystemRpc;
import com.method5.jot.query.model.RuntimeVersion;
import com.method5.jot.query.model.SystemProperties;
import com.method5.jot.rpc.PolkadotClient;

public class ChainSpecBuilder {
    private final PolkadotClient client;

    public ChainSpecBuilder(PolkadotClient client) {
        this.client = client;
    }

    public ChainSpec build() {
        ChainSpec spec = new ChainSpec();

        try {
            spec.setId(SystemRpc.chain(client));
            spec.setName(SystemRpc.name(client));

            spec.setGenesisHash(ChainRpc.getBlockHash(client, 0));

            SystemProperties systemProperties = SystemRpc.properties(client);
            spec.setSs58Prefix(systemProperties.getSs58Format());
            spec.setTokenDecimals(systemProperties.getTokenDecimals());
            spec.setTokenSymbol(systemProperties.getTokenSymbol());

            RuntimeVersion runtimeVersion = StateRpc.runtimeVersion(client);
            spec.setTransactionVersion(runtimeVersion.getTransactionVersion());
            spec.setSpecVersion(runtimeVersion.getSpecVersion());

            spec.setMetadata(StateRpc.metadata(client));
        } catch (Exception e) {
            throw new RuntimeException("Failed to build ChainSpec. " + e.getMessage(), e);
        }
        return spec;
    }
}
