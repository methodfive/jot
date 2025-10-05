package com.method5.jot.examples.subscription;

import com.method5.jot.examples.Config;
import com.method5.jot.query.model.BlockHeader;
import com.method5.jot.rpc.PolkadotWs;
import com.method5.jot.rpc.Subscription;
import com.method5.jot.rpc.SubscriptionType;
import com.method5.jot.util.ExampleBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SubscribeLatestHeadsExample extends ExampleBase {
    private static final Logger logger = LoggerFactory.getLogger(SubscribeLatestHeadsExample.class);

    public static void main(String[] args) throws Exception {
        try (PolkadotWs api = new PolkadotWs(Config.WSS_SERVER, 1000)) {
            execute(api);
        }
    }

    private static void execute(PolkadotWs api) throws Exception {
        logger.info("Subscribe Latest Heads Example");

        Thread.sleep(100);

        CountDownLatch latch = new CountDownLatch(2);

        // Subscribe to latest heads
        Subscription<BlockHeader> subscription = new Subscription<>(
                SubscriptionType.LATEST_HEAD,
                api,
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
