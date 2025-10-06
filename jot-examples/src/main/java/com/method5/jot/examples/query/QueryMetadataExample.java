package com.method5.jot.examples.query;

import com.method5.jot.rpc.PolkadotWs;
import com.method5.jot.util.ExampleBase;
import com.method5.jot.util.HexUtil;
import com.method5.jot.examples.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryMetadataExample extends ExampleBase {
    private static final Logger logger = LoggerFactory.getLogger(QueryMetadataExample.class);

    public static void main(String[] args) throws Exception {
        try (PolkadotWs api = new PolkadotWs(Config.WSS_SERVER, 10000)) {
            execute(api);
        }
    }

    public static void execute(PolkadotWs api) throws Exception {
        logger.info("Query Metadata Example");
        logger.info("------------------------");

        byte[] metadata = api.query().state().metadata();

        logger.info("Found metadata blob: {}...", HexUtil.bytesToHex(metadata).substring(0, Math.min(metadata.length, 256)));

        // See ParsingMetadataExample.java for parsing
    }
}
