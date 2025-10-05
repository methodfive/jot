package com.method5.jot.query;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.method5.jot.entity.Weight;
import com.method5.jot.query.model.FeeInfo;
import com.method5.jot.rpc.Api;
import com.method5.jot.rpc.CallOrQuery;

import java.math.BigInteger;

/**
 * PaymentRpc — class for payment rpc in the Jot SDK. Provides RPC client / JSON‑RPC integration;
 * fee estimation and payment info.
 */
public class PaymentRpc extends CallOrQuery {
    public PaymentRpc(Api api) {
        super(api);
    }

    public FeeInfo queryInfo(String extrinsic) throws Exception {
        return queryInfo(extrinsic, null);
    }

    public FeeInfo queryInfo(String extrinsic, String at) throws Exception {
        ArrayNode params = mapper.createArrayNode();
        params.add(extrinsic);
        if (at != null) {
            params.add(at);
        }

        JsonNode result = api.send("payment_queryInfo", params);
        Weight weight = new Weight(result.get("weight").get("ref_time").bigIntegerValue(), result.get("weight").get("proof_size").bigIntegerValue());

        return new FeeInfo(weight, new BigInteger(result.get("partialFee").asText()));
    }
}

