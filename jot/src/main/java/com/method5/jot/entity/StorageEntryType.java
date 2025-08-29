package com.method5.jot.entity;

/**
 * StorageEntryType — enum for storage entry type in the Jot SDK. Provides chain state / storage
 * queries; types and data models.
 */
public enum StorageEntryType {
    PLAIN(0, "plain"),
    MAP(1, "map");

    private final int type;
    private final String name;

    StorageEntryType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public static StorageEntryType fromId(int id) {
        for (StorageEntryType vt : values()) {
            if (vt.type == id) {
                return vt;
            }
        }
        throw new IllegalArgumentException("Unknown StorageEntryType id: " + id);
    }

    @Override
    public String toString() {
        return "StorageEntryType{" +
                "type=" + type +
                ", name='" + name + '\'' +
                '}';
    }
}