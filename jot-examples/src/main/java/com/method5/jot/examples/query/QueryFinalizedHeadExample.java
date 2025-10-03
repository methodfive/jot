package com.method5.jot.examples.query;

import com.method5.jot.query.ChainRpc;
import com.method5.jot.rpc.PolkadotRpcClient;
import com.method5.jot.util.HexUtil;
import com.method5.jot.examples.ExampleConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryFinalizedHeadExample {
    private static final Logger logger = LoggerFactory.getLogger(QueryFinalizedHeadExample.class);

    public static void main(String[] args) throws Exception {
        try (PolkadotRpcClient client = new PolkadotRpcClient(new String[] { ExampleConstants.RPC_SERVER }, 10000)) {

            byte[] blockHash = ChainRpc.getFinalizedHead(client);
            logger.info("Finalized head hash512: {}", HexUtil.bytesToHex(blockHash));
        }
    }
}
