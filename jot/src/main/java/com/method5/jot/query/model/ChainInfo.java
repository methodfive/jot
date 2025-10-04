package com.method5.jot.query.model;

/**
 * ChainInfo — class for chain info in the Jot SDK. Provides types and data models.
 */
public class ChainInfo {
    private String name;

    public ChainInfo(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}