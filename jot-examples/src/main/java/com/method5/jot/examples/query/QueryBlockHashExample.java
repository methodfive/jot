package com.method5.jot.examples.query;

import com.method5.jot.rpc.PolkadotWs;
import com.method5.jot.examples.Config;
import com.method5.jot.util.ExampleBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryBlockHashExample extends ExampleBase {
    private static final Logger logger = LoggerFactory.getLogger(QueryBlockHashExample.class);

    public static void main(String[] args) throws Exception {
        try (PolkadotWs api = new PolkadotWs(Config.WSS_SERVER, 10000)) {
            execute(api);
        }
    }

    public static void execute(PolkadotWs api) throws Exception {
        logger.info("Query Block Hash Example");
        logger.info("------------------------");

        int blockNumber = 100;

        String blockHash = api.query().chain().blockHash(blockNumber);

        logger.info("Block number: {}", blockNumber);
        logger.info("Found hash: {}", blockHash);
    }
}
