package com.method5.jot.query;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.method5.jot.rpc.PolkadotClient;
import com.method5.jot.query.model.FeeInfo;
import com.method5.jot.entity.Weight;

import java.math.BigInteger;

/**
 * PaymentRpc — class for payment rpc in the Jot SDK. Provides RPC client / JSON‑RPC integration;
 * fee estimation and payment info.
 */
public class PaymentRpc {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static FeeInfo queryInfo(PolkadotClient client, String extrinsic) throws Exception {
        return queryInfo(client, extrinsic, null);
    }

    public static FeeInfo queryInfo(PolkadotClient client, String extrinsic, String at) throws Exception {
        ArrayNode params = mapper.createArrayNode();
        params.add(extrinsic);
        if (at != null) {
            params.add(at);
        }

        JsonNode result = client.send("payment_queryInfo", params);
        Weight weight = new Weight(result.get("weight").get("ref_time").bigIntegerValue(), result.get("weight").get("proof_size").bigIntegerValue());

        return new FeeInfo(weight, new BigInteger(result.get("partialFee").asText()));
    }
}

