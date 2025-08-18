package com.method5.jot.rpc;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.UUID;

public class PolkadotRpcClient extends PolkadotClient {
    private final HttpClient httpClient;
    private boolean closed = false;

    public PolkadotRpcClient(String server) {
        this(new String[] { server }, 10000);
    }

    public PolkadotRpcClient(String[] servers, long timeoutInMillis) {
        super(servers, timeoutInMillis);

        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(this.timeoutInMillis))
                .build();
    }

    @Override
    public JsonNode send(String method, JsonNode params) throws RpcException {
        if (closed) {
            throw new IllegalStateException("RPC client is already closed");
        }

        int attempts = 0;
        Exception lastException = null;
        int startIndex = currentIndex.get();

        while (attempts < servers.length) {
            int index = (startIndex + attempts) % servers.length;
            String url = servers[index];

            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .timeout(Duration.ofSeconds(10))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(buildPayload(UUID.randomUUID().toString(), method, params)))
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() != 200) {
                    throw new IOException("RPC server returned HTTP " + response.statusCode());
                }

                JsonNode json = objectMapper.readTree(response.body());

                if (json.has("error")) {
                    throw new IOException("RPC error: " + json.get("error"));
                }

                currentIndex.set(index);
                return json.get("result");
            } catch (Exception e) {
                lastException = e;
                attempts++;
            }
        }

        throw new RpcException("All RPC endpoints failed", lastException);
    }

    public boolean isClosed() {
        return closed;
    }

    @Override
    public void close() {
        closed = true;
        if(httpClient != null) {
            httpClient.close();
        }
    }
}
