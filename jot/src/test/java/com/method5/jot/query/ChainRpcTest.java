package com.method5.jot.query;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;
import com.method5.jot.TestBase;
import com.method5.jot.query.model.BlockHeader;
import com.method5.jot.query.model.SignedBlock;
import com.method5.jot.rpc.PolkadotWsClient;
import com.method5.jot.util.HexUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ChainRpcTest extends TestBase {
    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testGetBlock() throws Exception {
        PolkadotWsClient client = mock(PolkadotWsClient.class);

        SignedBlock expectedSignedBlock = new SignedBlock();

        when(client.send("chain_getBlock", JsonNodeFactory.instance.arrayNode())).thenReturn(mapper.convertValue(expectedSignedBlock, JsonNode.class));
        when(client.getChainSpec()).thenReturn(chainSpec);

        SignedBlock signedBlock = ChainRpc.getBlock(client);

        assertNotNull(signedBlock);
        assertEquals(signedBlock, expectedSignedBlock);
    }

    @Test
    public void testGetBlockAt() throws Exception {
        PolkadotWsClient client = mock(PolkadotWsClient.class);

        SignedBlock expectedSignedBlock = new SignedBlock();

        ArrayNode params = mapper.createArrayNode();
        params.add("0x54adc3902b0e959e865b53651353ba655c1a17d465a6691e965bb374d9457df5");

        when(client.send("chain_getBlock", params)).thenReturn(mapper.convertValue(expectedSignedBlock, JsonNode.class));
        when(client.getChainSpec()).thenReturn(chainSpec);

        SignedBlock signedBlock = ChainRpc.getBlock(client, "0x54adc3902b0e959e865b53651353ba655c1a17d465a6691e965bb374d9457df5");

        assertNotNull(signedBlock);
        assertEquals(signedBlock, expectedSignedBlock);
    }

    @Test
    public void testGetBlockHash() throws Exception {
        PolkadotWsClient client = mock(PolkadotWsClient.class);

        when(client.send("chain_getBlockHash", JsonNodeFactory.instance.arrayNode())).thenReturn(new TextNode("0x54adc3902b0e959e865b53651353ba655c1a17d465a6691e965bb374d9457df5"));
        when(client.getChainSpec()).thenReturn(chainSpec);

        byte[] blockHash = ChainRpc.getBlockHash(client);

        assertNotNull(blockHash);
        assertEquals("54adc3902b0e959e865b53651353ba655c1a17d465a6691e965bb374d9457df5", HexUtil.bytesToHex(blockHash));
    }

    @Test
    public void testGetGenesisBlockHash() throws Exception {
        PolkadotWsClient client = mock(PolkadotWsClient.class);

        ArrayNode params = mapper.createArrayNode();
        String hexBlockNumber = "0x" + Long.toHexString(0);
        params.add(hexBlockNumber);

        when(client.send("chain_getBlockHash", params)).thenReturn(new TextNode("0x54adc3902b0e959e865b53651353ba655c1a17d465a6691e965bb374d9457df5"));
        when(client.getChainSpec()).thenReturn(chainSpec);

        byte[] blockHash = ChainRpc.getGenesisBlockHash(client);

        assertNotNull(blockHash);
        assertEquals("54adc3902b0e959e865b53651353ba655c1a17d465a6691e965bb374d9457df5", HexUtil.bytesToHex(blockHash));
    }

    @Test
    public void testGetHeader() throws Exception {
        PolkadotWsClient client = mock(PolkadotWsClient.class);

        BlockHeader expectedBlockHeader = new BlockHeader();

        when(client.send("chain_getHeader", JsonNodeFactory.instance.arrayNode())).thenReturn(mapper.convertValue(expectedBlockHeader, JsonNode.class));
        when(client.getChainSpec()).thenReturn(chainSpec);

        BlockHeader blockHeader = ChainRpc.getHeader(client);

        assertNotNull(blockHeader);
        assertEquals(blockHeader, expectedBlockHeader);
    }

    @Test
    public void testGetHeaderAt() throws Exception {
        PolkadotWsClient client = mock(PolkadotWsClient.class);

        BlockHeader expectedBlockHeader = new BlockHeader();

        ArrayNode params = mapper.createArrayNode();
        params.add("0x54adc3902b0e959e865b53651353ba655c1a17d465a6691e965bb374d9457df5");

        when(client.send("chain_getHeader", params)).thenReturn(mapper.convertValue(expectedBlockHeader, JsonNode.class));
        when(client.getChainSpec()).thenReturn(chainSpec);

        BlockHeader blockHeader = ChainRpc.getHeader(client, "0x54adc3902b0e959e865b53651353ba655c1a17d465a6691e965bb374d9457df5");

        assertNotNull(blockHeader);
        assertEquals(blockHeader, expectedBlockHeader);
    }
}
