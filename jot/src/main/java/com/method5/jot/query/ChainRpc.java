package com.method5.jot.query;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.method5.jot.query.model.BlockHeader;
import com.method5.jot.query.model.SignedBlock;
import com.method5.jot.rpc.Api;
import com.method5.jot.rpc.CallOrQuery;
import com.method5.jot.util.HexUtil;

/**
 * ChainRpc — class for chain rpc in the Jot SDK. Provides RPC client / JSON‑RPC integration; key
 * management and signing.
 */
public class ChainRpc extends CallOrQuery {
    public ChainRpc(Api api) {
        super(api);
    }

    public SignedBlock block() throws Exception {
        JsonNode result = api.send("chain_getBlock", mapper.createArrayNode());
        return mapper.treeToValue(result, SignedBlock.class);
    }

    public SignedBlock block(String at) throws Exception {
        ArrayNode params = mapper.createArrayNode();
        params.add(at);
        JsonNode result = api.send("chain_getBlock", params);
        return mapper.treeToValue(result, SignedBlock.class);
    }

    public String blockHash() throws Exception {
        JsonNode result = api.send("chain_getBlockHash", mapper.createArrayNode());
        return result.asText();
    }

    public String genesisBlockHash() throws Exception {
        return blockHash(0);
    }

    public String blockHash(long blockNumber) throws Exception {
        ArrayNode params = mapper.createArrayNode();
        String hexBlockNumber = "0x" + Long.toHexString(blockNumber);
        params.add(hexBlockNumber);
        JsonNode result = api.send("chain_getBlockHash", params);
        return result.asText();
    }

    public byte[] finalizedHead() throws Exception {
        return HexUtil.hexToBytes(api.send("chain_getFinalizedHead", mapper.createArrayNode()).asText());
    }

    public BlockHeader header() throws Exception {
        JsonNode result = api.send("chain_getHeader", mapper.createArrayNode());
        return mapper.treeToValue(result, BlockHeader.class);
    }

    public BlockHeader header(String at) throws Exception {
        ArrayNode params = mapper.createArrayNode();
        params.add(at);
        JsonNode result = api.send("chain_getHeader", params);
        return mapper.treeToValue(result, BlockHeader.class);
    }
}
