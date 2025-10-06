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

    public static void execute(PolkadotWs api) throws Exception {
        logger.info("Query Runtime Version Example");
        logger.info("------------------------");

        RuntimeVersion runtimeVersion = api.query().state().runtimeVersion();

        logger.info("Found spec name: {}", runtimeVersion.getSpecName());
        logger.info("Found impl name: {}", runtimeVersion.getImplName());
        logger.info("Found tx version: {}", runtimeVersion.getTransactionVersion());
        logger.info("Found impl version: {}", runtimeVersion.getImplVersion());
        logger.info("Found spec version: {}", runtimeVersion.getSpecVersion());
        logger.info("Found authoring version: {}", runtimeVersion.getAuthoringVersion());
    }
}
