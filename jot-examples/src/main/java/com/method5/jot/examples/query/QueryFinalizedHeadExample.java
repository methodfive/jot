package com.method5.jot.examples.query;

import com.method5.jot.rpc.PolkadotWs;
import com.method5.jot.util.ExampleBase;
import com.method5.jot.util.HexUtil;
import com.method5.jot.examples.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryFinalizedHeadExample extends ExampleBase {
    private static final Logger logger = LoggerFactory.getLogger(QueryFinalizedHeadExample.class);

    public static void main(String[] args) throws Exception {
        try (PolkadotWs api = new PolkadotWs(Config.WSS_SERVER, 10000)) {
            execute(api);
        }
    }

    private static void execute(PolkadotWs api) throws Exception {
        logger.info("Query Finalized Head Example");

        byte[] blockHash = api.query().chain().finalizedHead();
        logger.info("Finalized head hash: {}", HexUtil.bytesToHex(blockHash));
    }
}
