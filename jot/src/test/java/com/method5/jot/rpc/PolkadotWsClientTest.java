package com.method5.jot.rpc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.method5.jot.TestBase;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class PolkadotWsClientTest extends TestBase {

    @Test
    public void testCanCreateAndCloseWsClient() {
        try (PolkadotWsClient client = new PolkadotWsClient("ws://localhost:9944")) {
            assertNotNull(client);
        } catch (Exception e) {
            fail("Failed to initialize or close RPC client: " + e.getMessage());
        }
    }

    @Test
    public void testSendRequestHandlesConnectionFailureGracefully() {
        try (PolkadotWsClient client = new PolkadotWsClient("wss://invalid-endpoint")) {
            assertThrows(Exception.class, () -> {
                JsonNode emptyParams = new ObjectMapper().createArrayNode();
                client.send("system_chain", emptyParams);
            });
        }
    }

    @Test
    public void testConnectAndRetrieveData() {
        try (PolkadotWsClient client = new PolkadotWsClient(DOT_RPC_SERVERS, 1000)) {
            assertNotNull(client);
            Thread.sleep(100);

            JsonNode idNode = client.send("system_chain", JsonNodeFactory.instance.arrayNode());
            assertEquals("Polkadot", idNode.asText());
        } catch (Exception e) {
            fail("Failed to initialize or close RPC client: " + e.getMessage());
        }
    }

    @Test
    public void testRotateConnectionsAndRetrieveData() {
        String[] servers = new String[DOT_RPC_SERVERS.length + 1];
        servers[0] = "wss://invalid-endpoint";
        System.arraycopy(DOT_RPC_SERVERS, 0, servers, 1, DOT_RPC_SERVERS.length);

        try (PolkadotWsClient client = new PolkadotWsClient(servers, 1000)) {
            assertNotNull(client);
            Thread.sleep(100);

            JsonNode idNode = client.send("system_chain", JsonNodeFactory.instance.arrayNode());
            assertEquals("Polkadot", idNode.asText());
        } catch (Exception e) {
            fail("Failed to initialize or close RPC client: " + e.getMessage());
        }
    }

    @Test
    public void testSubscribe() {
        try (PolkadotWsClient client = new PolkadotWsClient(DOT_RPC_SERVERS, 1000)) {
            assertNotNull(client);
            Thread.sleep(100);

            CountDownLatch latch = new CountDownLatch(1);

            String subscriptionID = client.subscribe("chain_subscribeNewHeads", objectMapper.createArrayNode(), (JsonNode newHead) -> latch.countDown());

            if(!latch.await(6500, TimeUnit.MILLISECONDS)) {
                fail("Timeout waiting for subscribing new heads");
            }

            assertTrue(client.unsubscribe("chain_unsubscribeNewHead", subscriptionID));
        } catch (Exception e) {
            fail("Failed to initialize or close RPC client: " + e.getMessage());
        }
    }
}
