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

    private static void execute(PolkadotWs api) throws Exception {
        logger.info("Query Block Example");

        String blockHash = api.query().chain().genesisBlockHash();
        logger.info("Genesis hash: {}", blockHash);

        SignedBlock block = api.query().chain().block(blockHash);
        logger.info("Genesis block: {}", block);
    }
}
