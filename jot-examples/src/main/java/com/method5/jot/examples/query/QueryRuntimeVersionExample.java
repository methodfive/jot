package com.method5.jot.examples.query;

import com.method5.jot.query.model.RuntimeVersion;
import com.method5.jot.examples.Config;
import com.method5.jot.rpc.PolkadotWs;
import com.method5.jot.util.ExampleBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryRuntimeVersionExample extends ExampleBase {
    private static final Logger logger = LoggerFactory.getLogger(QueryRuntimeVersionExample.class);

    public static void main(String[] args) throws Exception {
        try (PolkadotWs api = new PolkadotWs(Config.WSS_SERVER, 10000)) {
            execute(api);
        }
    }

    private static void execute(PolkadotWs api) throws Exception {
        logger.info("Query Runtime Version Example");

        RuntimeVersion runtimeVersion = api.query().state().runtimeVersion();
        logger.info("Spec name: {}", runtimeVersion.getSpecName());
        logger.info("Impl name: {}", runtimeVersion.getImplName());
        logger.info("Transaction version: {}", runtimeVersion.getTransactionVersion());
        logger.info("Impl version: {}", runtimeVersion.getImplVersion());
        logger.info("Spec version: {}", runtimeVersion.getSpecVersion());
        logger.info("Authoring version: {}", runtimeVersion.getAuthoringVersion());
    }
}
