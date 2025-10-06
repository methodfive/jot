package com.method5.jot.examples.query;

import com.method5.jot.examples.Config;
import com.method5.jot.rpc.PolkadotWs;
import com.method5.jot.util.ExampleBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;

public class QueryNonceExample extends ExampleBase {
    private static final Logger logger = LoggerFactory.getLogger(QueryNonceExample.class);

    public static void main(String[] args) throws Exception {
        try (PolkadotWs api = new PolkadotWs(Config.WSS_SERVER, 10000)) {
            execute(api);
        }
    }

    public static void execute(PolkadotWs api) throws Exception {
        logger.info("Query Nonce Example");
        logger.info("------------------------");

        String address = "13NHcoGFJsHJoCYVsJrrv2ygLtz2XJSR17KrnA9QTNYz3Zkz";

        // Query nonce for wallet
        BigInteger nonce = api.query().system().accountNextIndex(address);

        logger.info("Address: {}", address);
        logger.info("Found nonce: {}", nonce);
    }
}
