package com.method5.jot.examples.query;

import com.method5.jot.query.model.BlockHeader;
import com.method5.jot.rpc.PolkadotWs;
import com.method5.jot.util.ExampleBase;
import com.method5.jot.util.HexUtil;
import com.method5.jot.examples.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryBlockHeaderExample extends ExampleBase {
    private static final Logger logger = LoggerFactory.getLogger(QueryBlockHeaderExample.class);

    public static void main(String[] args) throws Exception {
        try (PolkadotWs api = new PolkadotWs(Config.WSS_SERVER, 10000)) {
            execute(api);
        }
    }

    public static void execute(PolkadotWs api) throws Exception {
        logger.info("Query Block Header Example");
        logger.info("------------------------");

        byte[] blockHash = api.query().chain().finalizedHead();
        logger.info("Block hash: {}", HexUtil.bytesToHex(blockHash));

        BlockHeader blockHeader = api.query().chain().header(HexUtil.bytesToHex(blockHash));
        logger.info("Found block header: {}", blockHeader);
    }
}
