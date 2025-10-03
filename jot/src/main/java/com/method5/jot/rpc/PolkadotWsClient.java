package com.method5.jot.rpc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.method5.jot.crypto.Hasher;
import com.method5.jot.entity.DispatchError;
import com.method5.jot.events.EventRecord;
import com.method5.jot.extrinsic.ExtrinsicResult;
import com.method5.jot.metadata.RuntimeTypeDecoder;
import com.method5.jot.query.AuthorRpc;
import com.method5.jot.query.ChainRpc;
import com.method5.jot.query.StorageQuery;
import com.method5.jot.query.model.BlockHeader;
import com.method5.jot.query.model.SignedBlock;
import com.method5.jot.util.HexUtil;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * PolkadotWsClient — class for polkadot ws client in the Jot SDK. Provides RPC client / JSON‑RPC
 * integration; key management and signing; WebSocket subscriptions.
 */
public class PolkadotWsClient extends PolkadotClient implements AutoCloseable {
    private static Logger logger = LoggerFactory.getLogger(PolkadotWsClient.class);
    private final Map<String, Consumer<JsonNode>> responseHandlers = new ConcurrentHashMap<>();
    private final Map<String, Consumer<JsonNode>> subscriptionHandlers = new ConcurrentHashMap<>();

    private Timer pingTimer;
    private WebSocketClient client;

    public PolkadotWsClient(String server) {
        this(new String[] { server }, 10000);
    }

    public PolkadotWsClient(String[] servers, long timeoutInMillis) {
        super(servers, timeoutInMillis);
        connect();
    }

    private void connect() {
        Exception lastException = null;
        for (int i = 0; i < servers.length; i++) {
            int attemptIndex = (currentIndex.get() + i) % servers.length;
            try {
                this.client = getWebSocketClient(attemptIndex);
                this.currentIndex.set(attemptIndex);
                return;
            } catch (Exception e) {
                lastException = e;
            }
        }
        throw new RuntimeException("Failed to connect to any WebSocket server", lastException);
    }

    public enum Confirmation { BEST, FINALIZED }
    public List<EventRecord> waitForEvents(String extrinsicHash, long timeoutMs) throws Exception {
        return waitForEvents(Confirmation.BEST, extrinsicHash, timeoutMs);
    }

    public List<EventRecord> waitForEvents(Confirmation confirmation, String extrinsicHash, long timeoutMs) throws Exception {
        List<EventRecord> results = new ArrayList<>();
        CompletableFuture<Void> found = new CompletableFuture<>();

        Subscription<BlockHeader> subscription = new Subscription<>(
                confirmation == Confirmation.BEST ? SubscriptionType.BEST_HEAD : SubscriptionType.FINALIZED_HEAD,
                this,
                header -> {
                    try {
                        String blockHash = HexUtil.bytesToHex(ChainRpc.getBlockHash(this, header.getNumber()));
                        SignedBlock block = ChainRpc.getBlock(this, blockHash);

                        for (int i = 0; i < block.getBlock().getExtrinsics().size(); i++) {
                            String actualHash = HexUtil.bytesToHex(Hasher.hash256(HexUtil.hexToBytes(block.getBlock().getExtrinsics().get(i))));
                            if (HexUtil.trim(extrinsicHash).compareToIgnoreCase(actualHash) == 0) {

                                List<EventRecord> allEvents = StorageQuery.getSystemEvents(this, blockHash);
                                int finalI = i;
                                List<EventRecord> matchingEvents = allEvents.stream()
                                        .filter(ev -> ev.phase().isApplyExtrinsic(finalI))
                                        .toList();
                                results.addAll(matchingEvents);

                                found.complete(null);
                                return;
                            }
                        }
                    } catch (Exception ignored) {
                    }
                }
        );

        try {
            found.get(timeoutMs, TimeUnit.MILLISECONDS);
        } catch (TimeoutException ignored) {
        } finally {
            subscription.unsubscribe();
        }

        return results;
    }

    public ExtrinsicResult submitAndWaitForExtrinsic(byte[] extrinsic, Confirmation confirmation, long timeoutMs) throws Exception {
        String extrinsicHash = AuthorRpc.submitExtrinsic(this, extrinsic);
        return waitForExtrinsic(extrinsicHash, confirmation, timeoutMs);
    }

    public ExtrinsicResult waitForExtrinsic(String extrinsicHash, Confirmation confirmation, long timeoutMs) throws Exception {
        List<EventRecord> all = waitForEvents(confirmation, extrinsicHash, timeoutMs);

        boolean success = false;
        DispatchError error = null;

        for (EventRecord event : all) {
            if ("System".equals(event.pallet()) && "ExtrinsicSuccess".equals(event.method())) {
                success = true;
            }
            if ("System".equals(event.pallet()) && "ExtrinsicFailed".equals(event.method())) {
                RuntimeTypeDecoder.TypeAndValue typeAndValue = event.attributes().getOrDefault("DispatchError", null);
                if(typeAndValue != null) {
                    error = DispatchError.decode(typeAndValue, getResolver());
                }
            }
        }

        return new ExtrinsicResult(extrinsicHash, success, error, all);
    }

    private WebSocketClient getWebSocketClient(int attemptIndex) throws URISyntaxException, InterruptedException {
        String server = servers[attemptIndex];
        URI uri = new URI(server);
        WebSocketClient newClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake handshake) {}

            @Override
            public void onMessage(String message) {
                try {
                    JsonNode response = objectMapper.readTree(message);
                    if (response.has("id")) {
                        String id = response.get("id").textValue();
                        Consumer<JsonNode> handler = responseHandlers.remove(id);
                        if (handler != null) {
                            handler.accept(response);
                        } else {
                            logger.error("No handler found for id: " + id);
                        }
                    } else if (response.has("method") && response.has("params")) {
                        JsonNode params = response.get("params");
                        if (params.has("subscription")) {
                            String subscriptionId = params.get("subscription").asText();
                            Consumer<JsonNode> subHandler = subscriptionHandlers.get(subscriptionId);
                            if (subHandler != null) {
                                subHandler.accept(params.get("result"));
                            } else {
                                logger.error("No handler found for subscription: " + subscriptionId);
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.error("OnMessage failed:" + e.getMessage());
                }
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
            }

            @Override
            public void onError(Exception ex) {
            }
        };

        newClient.connectBlocking();

        pingTimer = new Timer("ping-timer", true);
        pingTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    if(client != null) {
                        client.sendPing();
                    }
                } catch (Exception ignored) {
                }
            }
        }, 0, 5000);

        return newClient;
    }

    public String subscribe(String method, JsonNode params, Consumer<JsonNode> onSubscriptionUpdate) throws Exception {
        String id = UUID.randomUUID().toString();

        CompletableFuture<String> future = new CompletableFuture<>();

        responseHandlers.put(id, (JsonNode initialResponse) -> {
            if (initialResponse.has("result")) {
                String subscriptionId = initialResponse.get("result").asText();
                subscriptionHandlers.put(subscriptionId, onSubscriptionUpdate);
                future.complete(subscriptionId);
            } else {
                logger.error("Subscription failed: " + initialResponse);
            }
        });

        client.send(buildPayload(String.valueOf(id), method, params));

        return future.get(timeoutInMillis, TimeUnit.MILLISECONDS);
    }

    public boolean unsubscribe(String subscriptionMethod, String subscriptionId) throws RpcException {
        ArrayNode params = objectMapper.createArrayNode();
        params.add(subscriptionId);

        JsonNode response = send(subscriptionMethod, params);
        return response.asBoolean();
    }

    public JsonNode send(String method, JsonNode params) throws RpcException {
        return send(UUID.randomUUID().toString(), method, params);
    }

    private JsonNode send(String id, String method, JsonNode params) throws RpcException {
        for (int i = 0; i < servers.length; i++) {
            try {
                CompletableFuture<JsonNode> future = new CompletableFuture<>();

                responseHandlers.put(id, future::complete);

                client.send(buildPayload(id, method, params));

                JsonNode result = future.get(timeoutInMillis, TimeUnit.MILLISECONDS);

                if(result == null) {
                    throw new TimeoutException("RPC timeout");
                }
                else if (result.has("error")) {
                    throw new RpcException("RPC Error: " + result.get("error").toString());
                }

                return result.get("result");
            } catch (WebsocketNotConnectedException | TimeoutException e) {
                rotateClient();
            } catch (ExecutionException | InterruptedException ignored) {
            }
        }
        throw new RpcException("WebSocket endpoints failed.");
    }

    private void rotateClient() {
        close();
        currentIndex.set((currentIndex.get() + 1) % servers.length);
        connect();
    }

    @Override
    public void close() {
        if (pingTimer != null) {
            pingTimer.cancel();
            pingTimer = null;
        }

        if (client != null) {
            try {
                client.closeBlocking();
            } catch (InterruptedException ignored) {
            }
        }
    }
}
