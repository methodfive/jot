package com.method5.jot.entity.metadata;

import java.util.ArrayList;
import java.util.List;

public class MetadataTypeField {
    private String name;
    private int type;
    private String typeName;
    private List<String> docs;

    public MetadataTypeField() {
        docs = new ArrayList<>();
    }

    public MetadataTypeField(String name, int type) {
        super();
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public List<String> getDocs() {
        return docs;
    }

    public void setDocs(List<String> docs) {
        this.docs = docs;
    }

    @Override
    public String toString() {
        return "MetadataTypeField{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", typeName='" + typeName + '\'' +
                ", docs=" + docs +
                '}';
    }
}