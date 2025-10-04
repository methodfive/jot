package com.method5.jot.query.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * RuntimeVersion — class for runtime version in the Jot SDK. Provides extrinsic construction and
 * submission; runtime metadata decoding; types and data models.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NetworkState {
    @JsonProperty("peerId")
    private String peerId;

    @JsonProperty("peers")
    private Map<String, PeerInfo> peers;

    @JsonProperty("listenedAddresses")
    private String[] listenedAddresses;

    @JsonProperty("externalAddresses")
    private String[] externalAddresses;

    @JsonProperty("connectedPeers")
    private String[] connectedPeers;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PeerInfo {
        @JsonProperty("enabled")
        private boolean enabled;

        @JsonProperty("knownAddresses")
        private String[] knownAddresses;

        @JsonProperty("latestPingTime")
        private Long latestPingTime;

        public boolean isEnabled() {
            return enabled;
        }

        public String[] getKnownAddresses() {
            return knownAddresses;
        }

        public Long getLatestPingTime() {
            return latestPingTime;
        }
    }

    public String getPeerId() {
        return peerId;
    }

    public Map<String, PeerInfo> getPeers() {
        return peers;
    }

    public String[] getListenedAddresses() {
        return listenedAddresses;
    }

    public String[] getExternalAddresses() {
        return externalAddresses;
    }

    public String[] getConnectedPeers() {
        return connectedPeers;
    }
}