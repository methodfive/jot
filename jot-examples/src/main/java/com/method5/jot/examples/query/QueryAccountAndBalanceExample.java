package com.method5.jot.examples.query;

import com.method5.jot.query.StorageQuery;
import com.method5.jot.query.model.AccountId;
import com.method5.jot.query.model.AccountInfo;
import com.method5.jot.rpc.PolkadotRpcClient;
import com.method5.jot.examples.ExampleConstants;

public class QueryAccountAndBalanceExample {
    public static void main(String[] args) throws Exception {
        try (PolkadotRpcClient client = new PolkadotRpcClient(new String[] { ExampleConstants.RPC_SERVER }, 10000)) {

            String polkadotTreasuryAddress = "13UVJyLnbVp9RBZYFwFGyDvVd1y27Tt8tkntv6Q7JVPhFsTB";
            AccountInfo accountInfo = StorageQuery.getAccountInfo(client, AccountId.fromSS58(polkadotTreasuryAddress));

            System.out.println("Polkadot treasury: " + polkadotTreasuryAddress);
            System.out.println("free balance: " + accountInfo.getFree());
            System.out.println("reserved balance: " + accountInfo.getReserved());
            System.out.println("frozen balance: " + accountInfo.getFrozen());
        }
    }
}
