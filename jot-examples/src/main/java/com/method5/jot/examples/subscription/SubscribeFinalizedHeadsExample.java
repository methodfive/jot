package com.method5.jot.examples.subscription;

import com.method5.jot.query.model.BlockHeader;
import com.method5.jot.rpc.PolkadotWsClient;
import com.method5.jot.rpc.Subscription;
import com.method5.jot.rpc.SubscriptionType;
import com.method5.jot.examples.ExampleConstants;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SubscribeFinalizedHeadsExample {
    public static void main(String[] args) throws Exception {
        try (PolkadotWsClient client = new PolkadotWsClient(new String[] { ExampleConstants.WSS_RPC_SERVER }, 1000)) {
            Thread.sleep(100);

            CountDownLatch latch = new CountDownLatch(2);

            // Subscribe to finalized heads
            Subscription<BlockHeader> subscription = new Subscription<>(
                    SubscriptionType.FINALIZED_HEAD,
                    client,
                    header -> {
                        System.out.println("New head: " + header);
                        latch.countDown();
                    }
            );

            // Wait until 2 finalized blocks are received (or we timeout)
            latch.await(6500, TimeUnit.MILLISECONDS);

            // Unsubscribe
            subscription.unsubscribe();
        }
    }
}
