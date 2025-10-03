package com.method5.jot.examples.query;

import com.method5.jot.query.StateRpc;
import com.method5.jot.query.model.RuntimeVersion;
import com.method5.jot.rpc.PolkadotRpcClient;
import com.method5.jot.examples.ExampleConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryRuntimeVersionExample {
    private static final Logger logger = LoggerFactory.getLogger(QueryRuntimeVersionExample.class);

    public static void main(String[] args) throws Exception {
        try (PolkadotRpcClient client = new PolkadotRpcClient(new String[] { ExampleConstants.RPC_SERVER }, 10000)) {

            RuntimeVersion runtimeVersion = StateRpc.runtimeVersion(client);
            logger.info("Spec name: {}", runtimeVersion.getSpecName());
            logger.info("Impl name: {}", runtimeVersion.getImplName());
            logger.info("Transaction version: {}", runtimeVersion.getTransactionVersion());
            logger.info("Impl version: {}", runtimeVersion.getImplVersion());
            logger.info("Spec version: {}", runtimeVersion.getSpecVersion());
            logger.info("Authoring version: {}", runtimeVersion.getAuthoringVersion());
        }
    }
}
