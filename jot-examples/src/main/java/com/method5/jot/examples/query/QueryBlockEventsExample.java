package com.method5.jot.examples.query;

import com.method5.jot.events.EventRecord;
import com.method5.jot.examples.ExampleConstants;
import com.method5.jot.query.StorageQuery;
import com.method5.jot.rpc.PolkadotRpcClient;

import java.util.List;

public class QueryBlockEventsExample {
    public static void main(String[] args) throws Exception {
        try (PolkadotRpcClient client = new PolkadotRpcClient(new String[] { ExampleConstants.RPC_SERVER }, 10000)) {

            // Query events for block hash
            List<EventRecord> events = StorageQuery.getSystemEvents(client, "0xc2e0040e731910193cd8f5f549967c9ac3d7d5bc8e6937154f3d12d76de3122d");

            System.out.println("Found " + events.size() + " events");

            for(EventRecord event : events) {
                System.out.println(event);
            }
        }
    }
}
