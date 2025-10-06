package com.method5.jot.examples.query;

import com.method5.jot.util.ExampleBase;
import com.method5.jot.query.model.AccountId;
import com.method5.jot.examples.Config;
import com.method5.jot.rpc.PolkadotWs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigInteger;

public class QueryAssetBalanceExample extends ExampleBase {
    private static final Logger logger = LoggerFactory.getLogger(QueryAssetBalanceExample.class);

    public static void main(String[] args) throws Exception {
        try (PolkadotWs api = new PolkadotWs(Config.ASSET_HUB_WSS_SERVER, 10000)) {
            execute(api);
        }
    }

    public static void execute(PolkadotWs api) throws Exception {
        logger.info("Query Asset Balance Example");
        logger.info("------------------------");

        String address = "5EMMKa93Qfu7Zk3wBryt8MiVH646auLMakb19wBMiSSAWE8U";
        BigInteger assetID = BigInteger.valueOf(31337); // USDC

        BigDecimal balance = api.query().storage().assetBalance(assetID, AccountId.fromSS58(address), 6);

        logger.info("Address: {}", address);
        logger.info("Balance: {} USDC", balance);
    }
}
