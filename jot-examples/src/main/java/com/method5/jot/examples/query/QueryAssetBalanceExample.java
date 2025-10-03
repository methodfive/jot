package com.method5.jot.examples.query;

import com.method5.jot.query.StorageQuery;
import com.method5.jot.query.model.AccountId;
import com.method5.jot.rpc.PolkadotRpcClient;
import com.method5.jot.examples.ExampleConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigInteger;

public class QueryAssetBalanceExample {
    private static final Logger logger = LoggerFactory.getLogger(QueryAssetBalanceExample.class);

    public static void main(String[] args) throws Exception {
        try (PolkadotRpcClient client = new PolkadotRpcClient(new String[] { ExampleConstants.ASSET_HUB_RPC_SERVER }, 10000)) {

            String polkadotTreasuryAddress = "14xmwinmCEz6oRrFdczHKqHgWNMiCysE2KrA4jXXAAM1Eogk";
            BigInteger assetID = BigInteger.valueOf(1984); // USDT

            BigDecimal balance = StorageQuery.getAssetBalance(client, assetID, AccountId.fromSS58(polkadotTreasuryAddress), 6);

            logger.info("Polkadot treasury: {}", polkadotTreasuryAddress);
            logger.info("USDT balance: {}", balance);
        }
    }
}
