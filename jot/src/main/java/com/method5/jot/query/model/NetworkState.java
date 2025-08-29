package com.method5.jot.query.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
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

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String[] getKnownAddresses() {
            return knownAddresses;
        }

        public void setKnownAddresses(String[] knownAddresses) {
            this.knownAddresses = knownAddresses;
        }

        public Long getLatestPingTime() {
            return latestPingTime;
        }

        public void setLatestPingTime(Long latestPingTime) {
            this.latestPingTime = latestPingTime;
        }

        @Override
        public String toString() {
            return "PeerInfo{" +
                    "enabled=" + enabled +
                    ", knownAddresses=" + Arrays.toString(knownAddresses) +
                    ", latestPingTime=" + latestPingTime +
                    '}';
        }
    }

    public String getPeerId() {
        return peerId;
    }

    public void setPeerId(String peerId) {
        this.peerId = peerId;
    }

    public Map<String, PeerInfo> getPeers() {
        return peers;
    }

    public void setPeers(Map<String, PeerInfo> peers) {
        this.peers = peers;
    }

    public String[] getListenedAddresses() {
        return listenedAddresses;
    }

    public void setListenedAddresses(String[] listenedAddresses) {
        this.listenedAddresses = listenedAddresses;
    }

    public String[] getExternalAddresses() {
        return externalAddresses;
    }

    public void setExternalAddresses(String[] externalAddresses) {
        this.externalAddresses = externalAddresses;
    }

    public String[] getConnectedPeers() {
        return connectedPeers;
    }

    public void setConnectedPeers(String[] connectedPeers) {
        this.connectedPeers = connectedPeers;
    }

    @Override
    public String toString() {
        return "NetworkState{" +
                "peerId='" + peerId + '\'' +
                ", peers=" + peers +
                ", listenedAddresses=" + Arrays.toString(listenedAddresses) +
                ", externalAddresses=" + Arrays.toString(externalAddresses) +
                ", connectedPeers=" + Arrays.toString(connectedPeers) +
                '}';
    }
}