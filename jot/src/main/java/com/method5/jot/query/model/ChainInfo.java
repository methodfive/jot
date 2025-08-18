package com.method5.jot.query.model;

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

    @Override
    public String toString() {
        return "ChainInfo{" +
                "name='" + name + '\'' +
                '}';
    }
}