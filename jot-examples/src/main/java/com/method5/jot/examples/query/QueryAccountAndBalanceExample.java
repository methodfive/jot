package com.method5.jot.examples.query;

import com.method5.jot.query.StorageQuery;
import com.method5.jot.query.model.AccountId;
import com.method5.jot.query.model.AccountInfo;
import com.method5.jot.rpc.PolkadotRpcClient;
import com.method5.jot.examples.ExampleConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryAccountAndBalanceExample {
    private static final Logger logger = LoggerFactory.getLogger(QueryAccountAndBalanceExample.class);

    public static void main(String[] args) throws Exception {
        try (PolkadotRpcClient client = new PolkadotRpcClient(new String[] { ExampleConstants.RPC_SERVER }, 10000)) {

            String polkadotTreasuryAddress = "13UVJyLnbVp9RBZYFwFGyDvVd1y27Tt8tkntv6Q7JVPhFsTB";
            AccountInfo accountInfo = StorageQuery.getAccountInfo(client, AccountId.fromSS58(polkadotTreasuryAddress));

            logger.info("Polkadot treasury: {}", polkadotTreasuryAddress);
            logger.info("free balance: {}", accountInfo.getFree());
            logger.info("reserved balance: {}", accountInfo.getReserved());
            logger.info("frozen balance: {}", accountInfo.getFrozen());
        }
    }
}
