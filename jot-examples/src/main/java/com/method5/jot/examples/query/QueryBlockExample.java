package com.method5.jot.examples.query;

import com.method5.jot.query.model.SignedBlock;
import com.method5.jot.rpc.PolkadotWs;
import com.method5.jot.examples.Config;
import com.method5.jot.util.ExampleBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryBlockExample extends ExampleBase {
    private static final Logger logger = LoggerFactory.getLogger(QueryBlockExample.class);

    public static void main(String[] args) throws Exception {
        try (PolkadotWs api = new PolkadotWs(Config.WSS_SERVER, 10000)) {
            execute(api);
        }
    }

    public static void execute(PolkadotWs api) throws Exception {
        logger.info("Query Block Example");
        logger.info("------------------------");

        String blockHash = api.query().chain().genesisBlockHash();
        logger.info("Found genesis hash: {}", blockHash);

        SignedBlock block = api.query().chain().block(blockHash);
        logger.info("Found genesis block: {}", block);
    }
}
