package com.method5.jot.extrinsic;

import com.method5.jot.entity.DispatchError;
import com.method5.jot.events.EventRecord;

import java.util.List;

/**
 * ExtrinsicResult — class for extrinsic result in the Jot SDK. Provides extrinsic construction and
 * submission.
 */
public class ExtrinsicResult {
    private boolean success;
    private DispatchError error;
    private List<EventRecord> events;
    private String hash;

    public ExtrinsicResult(String hash, boolean success, DispatchError error, List<EventRecord> events) {
        this.hash = hash;
        this.success = success;
        this.error = error;
        this.events = events;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public DispatchError getError() {
        return error;
    }

    public void setError(DispatchError error) {
        this.error = error;
    }

    public List<EventRecord> getEvents() {
        return events;
    }

    public void setEvents(List<EventRecord> events) {
        this.events = events;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @Override
    public String toString() {
        return "ExtrinsicResult{" +
                "success=" + success +
                ", error=" + error +
                ", hash=" + hash +
                ", events=" + events +
                '}';
    }
}
