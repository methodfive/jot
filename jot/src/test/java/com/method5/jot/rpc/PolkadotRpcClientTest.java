package com.method5.jot.rpc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.method5.jot.TestBase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PolkadotRpcClientTest extends TestBase {
    @Test
    public void testCanCreateAndCloseRpcClient() {
        try (PolkadotRpc client = new PolkadotRpc(HTTPS_DOT_RPC_SERVERS, 10000)) {
            assertNotNull(client);

        } catch (Exception e) {
            fail("Failed to initialize or close RPC client: " + e.getMessage());
        }
    }

    @Test
    public void testSendRequestHandlesConnectionFailureGracefully() {
        try (PolkadotRpc client = new PolkadotRpc("http://invalid-endpoint")) {
            assertThrows(Exception.class, () -> {
                JsonNode emptyParams = new ObjectMapper().createArrayNode();
                client.send("system_chain", emptyParams);
            });
        }
    }

    @Test
    public void testConnectAndRetrieveData() {
        try (PolkadotRpc api = new PolkadotRpc(HTTPS_DOT_RPC_SERVERS, 1000)) {
            assertNotNull(api);

            assertEquals("Polkadot", api.query().system().chain());
        } catch (Exception e) {
            fail("Failed to initialize or close RPC client: " + e.getMessage());
        }
    }

    @Test
    public void testConnectAndRetrieveMetadata() {
        try (PolkadotRpc api = new PolkadotRpc(HTTPS_DOT_RPC_SERVERS, 1000)) {
            assertNotNull(api);

            assertNotNull(api.getMetadata());
            assertNotNull(api.getResolver());
            assertNotNull(api.getChainSpec());
        } catch (Exception e) {
            fail("Failed to initialize or close RPC client: " + e.getMessage());
        }
    }

    @Test
    public void testRotateConnectionsAndRetrieveData() {
        String[] servers = new String[HTTPS_DOT_RPC_SERVERS.length + 1];
        servers[0] = "http://invalid-endpoint";
        System.arraycopy(HTTPS_DOT_RPC_SERVERS, 0, servers, 1, HTTPS_DOT_RPC_SERVERS.length);

        try (PolkadotRpc client = new PolkadotRpc(servers, 1000)) {
            assertNotNull(client);

            JsonNode idNode = client.send("system_chain", JsonNodeFactory.instance.arrayNode());
            assertEquals("Polkadot", idNode.asText());
        } catch (Exception e) {
            fail("Failed to initialize or close RPC client: " + e.getMessage());
        }
    }

    @Test
    public void testNoServers() {
        assertThrows(Exception.class, () -> new PolkadotRpc(null, 1000));
    }
}
