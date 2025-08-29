package com.method5.jot.query;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.method5.jot.query.model.BlockHeader;
import com.method5.jot.query.model.SignedBlock;
import com.method5.jot.rpc.PolkadotClient;
import com.method5.jot.util.HexUtil;

public class ChainRpc {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static SignedBlock getBlock(PolkadotClient client) throws Exception {
        JsonNode result = client.send("chain_getBlock", mapper.createArrayNode());
        return mapper.treeToValue(result, SignedBlock.class);
    }

    public static SignedBlock getBlock(PolkadotClient client, String at) throws Exception {
        ArrayNode params = mapper.createArrayNode();
        params.add(at);
        JsonNode result = client.send("chain_getBlock", params);
        return mapper.treeToValue(result, SignedBlock.class);
    }

    public static byte[] getBlockHash(PolkadotClient client) throws Exception {
        JsonNode result = client.send("chain_getBlockHash", mapper.createArrayNode());
        return HexUtil.hexToBytes(result.asText());
    }

    public static byte[] getGenesisBlockHash(PolkadotClient client) throws Exception {
        return getBlockHash(client, 0);
    }

    public static byte[] getBlockHash(PolkadotClient client, long blockNumber) throws Exception {
        ArrayNode params = mapper.createArrayNode();
        String hexBlockNumber = "0x" + Long.toHexString(blockNumber);
        params.add(hexBlockNumber);
        JsonNode result = client.send("chain_getBlockHash", params);
        return HexUtil.hexToBytes(result.asText());
    }

    public static byte[] getFinalizedHead(PolkadotClient client) throws Exception {
        return HexUtil.hexToBytes(client.send("chain_getFinalizedHead", mapper.createArrayNode()).asText());
    }

    public static BlockHeader getHeader(PolkadotClient client) throws Exception {
        JsonNode result = client.send("chain_getHeader", mapper.createArrayNode());
        return mapper.treeToValue(result, BlockHeader.class);
    }

    public static BlockHeader getHeader(PolkadotClient client, String at) throws Exception {
        ArrayNode params = mapper.createArrayNode();
        params.add(at);
        JsonNode result = client.send("chain_getHeader", params);
        return mapper.treeToValue(result, BlockHeader.class);
    }
}
