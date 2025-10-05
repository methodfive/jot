package com.method5.jot.examples.query;

import com.method5.jot.events.EventRecord;
import com.method5.jot.util.ExampleBase;
import com.method5.jot.examples.Config;
import com.method5.jot.rpc.PolkadotWs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class QueryBlockEventsExample extends ExampleBase {
    private static final Logger logger = LoggerFactory.getLogger(QueryBlockEventsExample.class);

    public static void main(String[] args) throws Exception {
        try (PolkadotWs api = new PolkadotWs(Config.WSS_SERVER, 10000)) {
            execute(api);
        }
    }

    private static void execute(PolkadotWs api) throws Exception {
        logger.info("Query Block Events Example");

        // Query events for block hash
        List<EventRecord> events = api.query().storage().systemEvents("0xccebfcc8199ceded491460e3279d14433f29f861885934ff01ad1952d788a274");

        logger.info("Found {} events", events.size());

        for(EventRecord event : events) {
            logger.info(event.toString());
        }
    }
}
