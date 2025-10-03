package com.method5.jot.examples.query;

import com.method5.jot.query.StateRpc;
import com.method5.jot.rpc.PolkadotRpcClient;
import com.method5.jot.util.HexUtil;
import com.method5.jot.examples.ExampleConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryMetadataExample {
    private static final Logger logger = LoggerFactory.getLogger(QueryMetadataExample.class);

    public static void main(String[] args) throws Exception {
        try (PolkadotRpcClient client = new PolkadotRpcClient(new String[] { ExampleConstants.RPC_SERVER }, 10000)) {

            byte[] metadata = StateRpc.metadata(client);
            logger.info("Metadata: {}", HexUtil.bytesToHex(metadata));

            // See ParsingMetadataExample.java for parsing
        }
    }
}
