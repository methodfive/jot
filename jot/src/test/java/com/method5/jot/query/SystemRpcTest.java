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
import com.method5.jot.rpc.PolkadotWs;
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
        PolkadotWs api = mock(PolkadotWs.class);

        ArrayNode params = mapper.createArrayNode();
        params.add("13UVJyLnbVp9RBZYFwFGyDvVd1y27Tt8tkntv6Q7JVPhFsTB");

        when(api.send("system_accountNextIndex", params)).thenReturn(new BigIntegerNode(BigInteger.TEN));
        when(api.getChainSpec()).thenReturn(chainSpec);
        when(api.query()).thenReturn(new Query(api));

        BigInteger nextIndex = api.query().system().accountNextIndex("13UVJyLnbVp9RBZYFwFGyDvVd1y27Tt8tkntv6Q7JVPhFsTB");

        assertNotNull(nextIndex);
        assertEquals(BigInteger.TEN, nextIndex);
    }

    @Test
    public void testVersion() throws Exception {
        PolkadotWs api = mock(PolkadotWs.class);

        when(api.send("system_version", JsonNodeFactory.instance.arrayNode())).thenReturn(new TextNode("12345"));
        when(api.getChainSpec()).thenReturn(chainSpec);
        when(api.query()).thenReturn(new Query(api));

        String version = api.query().system().version();

        assertNotNull(version);
        assertEquals("12345", version);
    }

    @Test
    public void testChain() throws Exception {
        PolkadotWs api = mock(PolkadotWs.class);

        when(api.send("system_chain", JsonNodeFactory.instance.arrayNode())).thenReturn(new TextNode("test"));
        when(api.getChainSpec()).thenReturn(chainSpec);
        when(api.query()).thenReturn(new Query(api));

        String chain = api.query().system().chain();

        assertNotNull(chain);
        assertEquals("test", chain);
    }

    @Test
    public void testName() throws Exception {
        PolkadotWs api = mock(PolkadotWs.class);

        when(api.send("system_name", JsonNodeFactory.instance.arrayNode())).thenReturn(new TextNode("test"));
        when(api.getChainSpec()).thenReturn(chainSpec);
        when(api.query()).thenReturn(new Query(api));

        String chain = api.query().system().name();

        assertNotNull(chain);
        assertEquals("test", chain);
    }

    @Test
    public void testProperties() throws Exception {
        PolkadotWs api = mock(PolkadotWs.class);

        SystemProperties expectedProperties = new  SystemProperties();
        expectedProperties.setTokenDecimals(99);
        expectedProperties.setTokenSymbol("DOT");

        when(api.send("system_properties", JsonNodeFactory.instance.arrayNode())).thenReturn(mapper.convertValue(expectedProperties, JsonNode.class));
        when(api.getChainSpec()).thenReturn(chainSpec);
        when(api.query()).thenReturn(new Query(api));

        SystemProperties properties = api.query().system().properties();

        assertNotNull(properties);
        assertEquals(99, properties.getTokenDecimals());
        assertEquals("DOT", properties.getTokenSymbol());
    }

    @Test
    public void testChainType() throws Exception {
        PolkadotWs api = mock(PolkadotWs.class);

        ChainType expectedChainType = ChainType.DEVELOPMENT;

        when(api.send("system_chainType", JsonNodeFactory.instance.arrayNode())).thenReturn(mapper.convertValue(expectedChainType, JsonNode.class));
        when(api.getChainSpec()).thenReturn(chainSpec);
        when(api.query()).thenReturn(new Query(api));

        ChainType chainType = api.query().system().chainType();

        assertNotNull(chainType);
        assertEquals(expectedChainType, chainType);
    }

    @Test
    public void testSystemHealth() throws Exception {
        PolkadotWs api = mock(PolkadotWs.class);

        SystemHealth expectedHealth = new SystemHealth();

        when(api.send("system_health", JsonNodeFactory.instance.arrayNode())).thenReturn(mapper.convertValue(expectedHealth, JsonNode.class));
        when(api.getChainSpec()).thenReturn(chainSpec);
        when(api.query()).thenReturn(new Query(api));

        SystemHealth systemHealth = api.query().system().health();

        assertNotNull(systemHealth);
        assertEquals(systemHealth, expectedHealth);
    }
}
