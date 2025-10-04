package com.method5.jot.query.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * SystemHealth — class for system health in the Jot SDK. Provides types and data models.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SystemHealth {
    @JsonProperty("isSyncing")
    private boolean isSyncing;

    @JsonProperty("peers")
    private int peers;

    @JsonProperty("shouldHavePeers")
    private boolean shouldHavePeers;

    @JsonProperty("diskUsage")
    private long diskUsage;

    @JsonProperty("isOffline")
    private boolean isOffline;

    public boolean isSyncing() {
        return isSyncing;
    }

    public void setSyncing(boolean syncing) {
        isSyncing = syncing;
    }

    public int getPeers() {
        return peers;
    }

    public void setPeers(int peers) {
        this.peers = peers;
    }

    public boolean isShouldHavePeers() {
        return shouldHavePeers;
    }

    public void setShouldHavePeers(boolean shouldHavePeers) {
        this.shouldHavePeers = shouldHavePeers;
    }

    public long getDiskUsage() {
        return diskUsage;
    }

    public void setDiskUsage(long diskUsage) {
        this.diskUsage = diskUsage;
    }

    public boolean isOffline() {
        return isOffline;
    }

    public void setOffline(boolean offline) {
        isOffline = offline;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SystemHealth that = (SystemHealth) o;
        return isSyncing == that.isSyncing && peers == that.peers && shouldHavePeers == that.shouldHavePeers && diskUsage == that.diskUsage && isOffline == that.isOffline;
    }
}