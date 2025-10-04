package com.method5.jot.query;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BigIntegerNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;
import com.method5.jot.TestBase;
import com.method5.jot.query.model.ChainType;
import com.method5.jot.query.model.SystemHealth;
import com.method5.jot.query.model.SystemProperties;
import com.method5.jot.rpc.PolkadotWsClient;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SystemRpcTest extends TestBase {
    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testAccountNextIndex() throws Exception {
        PolkadotWsClient client = mock(PolkadotWsClient.class);

        ArrayNode params = mapper.createArrayNode();
        params.add("13UVJyLnbVp9RBZYFwFGyDvVd1y27Tt8tkntv6Q7JVPhFsTB");

        when(client.send("system_accountNextIndex", params)).thenReturn(new BigIntegerNode(BigInteger.TEN));
        when(client.getChainSpec()).thenReturn(chainSpec);

        BigInteger nextIndex = SystemRpc.accountNextIndex(client, "13UVJyLnbVp9RBZYFwFGyDvVd1y27Tt8tkntv6Q7JVPhFsTB");

        assertNotNull(nextIndex);
        assertEquals(BigInteger.TEN, nextIndex);
    }

    @Test
    public void testVersion() throws Exception {
        PolkadotWsClient client = mock(PolkadotWsClient.class);

        when(client.send("system_version", JsonNodeFactory.instance.arrayNode())).thenReturn(new TextNode("12345"));
        when(client.getChainSpec()).thenReturn(chainSpec);

        String version = SystemRpc.version(client);

        assertNotNull(version);
        assertEquals("12345", version);
    }

    @Test
    public void testChain() throws Exception {
        PolkadotWsClient client = mock(PolkadotWsClient.class);

        when(client.send("system_chain", JsonNodeFactory.instance.arrayNode())).thenReturn(new TextNode("test"));
        when(client.getChainSpec()).thenReturn(chainSpec);

        String chain = SystemRpc.chain(client);

        assertNotNull(chain);
        assertEquals("test", chain);
    }

    @Test
    public void testName() throws Exception {
        PolkadotWsClient client = mock(PolkadotWsClient.class);

        when(client.send("system_name", JsonNodeFactory.instance.arrayNode())).thenReturn(new TextNode("test"));
        when(client.getChainSpec()).thenReturn(chainSpec);

        String chain = SystemRpc.name(client);

        assertNotNull(chain);
        assertEquals("test", chain);
    }

    @Test
    public void testProperties() throws Exception {
        PolkadotWsClient client = mock(PolkadotWsClient.class);

        SystemProperties expectedProperties = new  SystemProperties();
        expectedProperties.setTokenDecimals(99);
        expectedProperties.setTokenSymbol("DOT");

        when(client.send("system_properties", JsonNodeFactory.instance.arrayNode())).thenReturn(mapper.convertValue(expectedProperties, JsonNode.class));
        when(client.getChainSpec()).thenReturn(chainSpec);

        SystemProperties properties = SystemRpc.properties(client);

        assertNotNull(properties);
        assertEquals(99, properties.getTokenDecimals());
        assertEquals("DOT", properties.getTokenSymbol());
    }

    @Test
    public void testChainType() throws Exception {
        PolkadotWsClient client = mock(PolkadotWsClient.class);

        ChainType expectedChainType = ChainType.DEVELOPMENT;

        when(client.send("system_chainType", JsonNodeFactory.instance.arrayNode())).thenReturn(mapper.convertValue(expectedChainType, JsonNode.class));
        when(client.getChainSpec()).thenReturn(chainSpec);

        ChainType chainType = SystemRpc.chainType(client);

        assertNotNull(chainType);
        assertEquals(expectedChainType, chainType);
    }

    @Test
    public void testSystemHealth() throws Exception {
        PolkadotWsClient client = mock(PolkadotWsClient.class);

        SystemHealth expectedHealth = new SystemHealth();

        when(client.send("system_health", JsonNodeFactory.instance.arrayNode())).thenReturn(mapper.convertValue(expectedHealth, JsonNode.class));
        when(client.getChainSpec()).thenReturn(chainSpec);

        SystemHealth systemHealth = SystemRpc.health(client);

        assertNotNull(systemHealth);
        assertEquals(systemHealth, expectedHealth);
    }
}
