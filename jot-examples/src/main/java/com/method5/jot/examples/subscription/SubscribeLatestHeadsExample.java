package com.method5.jot.examples.subscription;

import com.method5.jot.query.model.BlockHeader;
import com.method5.jot.rpc.PolkadotWsClient;
import com.method5.jot.rpc.Subscription;
import com.method5.jot.rpc.SubscriptionType;
import com.method5.jot.examples.ExampleConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SubscribeLatestHeadsExample {
    private static final Logger logger = LoggerFactory.getLogger(SubscribeLatestHeadsExample.class);

    public static void main(String[] args) throws Exception {
        try (PolkadotWsClient client = new PolkadotWsClient(new String[] { ExampleConstants.WSS_RPC_SERVER }, 1000)) {
            Thread.sleep(100);

            CountDownLatch latch = new CountDownLatch(2);

            // Subscribe to latest heads
            Subscription<BlockHeader> subscription = new Subscription<>(
                    SubscriptionType.LATEST_HEAD,
                    client,
                    header -> {
                        logger.info("New head: {}", header);
                        latch.countDown();
                    }
            );

            // Wait until 2 blocks are received (or we timeout)
            latch.await(6500, TimeUnit.MILLISECONDS);

            // Unsubscribe
            subscription.unsubscribe();
        }
    }
}
