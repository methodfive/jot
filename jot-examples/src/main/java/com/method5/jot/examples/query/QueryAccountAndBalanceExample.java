package com.method5.jot.examples.query;

import com.method5.jot.util.ExampleBase;
import com.method5.jot.query.model.AccountId;
import com.method5.jot.query.model.AccountInfo;
import com.method5.jot.examples.Config;
import com.method5.jot.rpc.PolkadotWs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryAccountAndBalanceExample extends ExampleBase {
    private static final Logger logger = LoggerFactory.getLogger(QueryAccountAndBalanceExample.class);

    public static void main(String[] args) throws Exception {
        try (PolkadotWs api = new PolkadotWs(Config.WSS_SERVER, 10000)) {
            execute(api);
        }
    }

    private static void execute(PolkadotWs api) throws Exception {
        logger.info("Query Account and Balance Example");

        String address = "5CfP97wzNCKkgHsTpD8geoqnKnNv9DydD1y5yfgqtAeVJzMm";

        AccountInfo accountInfo = api.query().storage().accountInfo(AccountId.fromSS58(address));

        logger.info("Address: {}", address);
        logger.info("free balance: {}", accountInfo.getFree());
        logger.info("reserved balance: {}", accountInfo.getReserved());
        logger.info("frozen balance: {}", accountInfo.getFrozen());
    }
}
