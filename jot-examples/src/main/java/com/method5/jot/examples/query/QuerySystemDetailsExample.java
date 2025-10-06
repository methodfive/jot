package com.method5.jot.examples.query;

import com.method5.jot.examples.Config;
import com.method5.jot.query.model.ChainType;
import com.method5.jot.query.model.SystemHealth;
import com.method5.jot.query.model.SystemProperties;
import com.method5.jot.rpc.PolkadotWs;
import com.method5.jot.util.ExampleBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuerySystemDetailsExample extends ExampleBase {
    private static final Logger logger = LoggerFactory.getLogger(QuerySystemDetailsExample.class);

    public static void main(String[] args) throws Exception {
        try (PolkadotWs api = new PolkadotWs(Config.WSS_SERVER, 10000)) {
            execute(api);
        }
    }

    public static void execute(PolkadotWs api) throws Exception {
        logger.info("Query System Details Example");
        logger.info("------------------------");

        String chain = api.query().system().chain();
        logger.info("Found chain: {}", chain);

        String name = api.query().system().name();
        logger.info("Found node name: {}", name);

        ChainType chainType = api.query().system().chainType();
        logger.info("Found chain type: {}", chainType.getValue());

        SystemHealth systemHealth = api.query().system().health();
        logger.info("Found system health: {}", systemHealth);

        String version = api.query().system().version();
        logger.info("Found node version: {}", version);

        SystemProperties properties = api.query().system().properties();
        logger.info("System properties: {}", properties);
    }
}
