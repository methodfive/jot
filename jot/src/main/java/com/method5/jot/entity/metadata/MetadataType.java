package com.method5.jot.entity.metadata;

import com.method5.jot.scale.ScaleReader;
import com.method5.jot.scale.ScaleWriter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class MetadataType {
    private int id;
    private List<String> path;
    private List<Param> params;
    private MetadataTypeDefinition def;
    private List<String> docs;

    public MetadataType() {
        path = new ArrayList<>();
        params = new ArrayList<>();
        docs = new ArrayList<>();
    }

    public MetadataType(int id, MetadataTypeDefinition def) {
        this.id = id;
        this.def = def;
    }

    public static MetadataType decode(ScaleReader reader) {
        MetadataType t = new MetadataType();
        t.setId(reader.readCompact().intValue());
        t.setPath(reader.readStringList());

        int paramCount = reader.readCompact().intValue();
        for (int i = 0; i < paramCount; i++) {
            t.getParams().add(Param.decode(reader));
        }

        t.setDef(MetadataTypeDefinition.decode(reader));
        t.setDocs(reader.readStringList());
        return t;
    }

    public byte[] encode() {
        ScaleWriter scaleWriter = new ScaleWriter();
        scaleWriter.writeCompact(BigInteger.valueOf(getId()));
        scaleWriter.writeStringList(getPath());
        scaleWriter.writeCompact(BigInteger.valueOf(getParams().size()));
        for(Param param : getParams()) {
            scaleWriter.writeBytes(param.encode());
        }
        scaleWriter.writeBytes(getDef().encode());
        scaleWriter.writeStringList(getDocs());
        return scaleWriter.toByteArray();
    }

    public static class Param {
        private String name;
        private Integer type;

        public static Param decode(ScaleReader reader) {
            Param param = new Param();
            param.setName(reader.readString());
            if (reader.readBoolean()) {
                param.setType(reader.readCompact().intValue());
            }
            return param;
        }

        public byte[] encode() {
            ScaleWriter writer = new ScaleWriter();
            writer.writeString(getName());

            writer.writeByte(getType() == null ? 0 : 1);
            if(getType() != null) {
                writer.writeCompact(BigInteger.valueOf(getType()));
            }
            return writer.toByteArray();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return "Param{" +
                    "name='" + name + '\'' +
                    ", type=" + type +
                    '}';
        }
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getPath() {
        return path;
    }

    public void setPath(List<String> path) {
        this.path = path;
    }

    public List<Param> getParams() {
        return params;
    }

    public void setParams(List<Param> params) {
        this.params = params;
    }

    public MetadataTypeDefinition getDef() {
        return def;
    }

    public void setDef(MetadataTypeDefinition def) {
        this.def = def;
    }

    public List<String> getDocs() {
        return docs;
    }

    public void setDocs(List<String> docs) {
        this.docs = docs;
    }

    @Override
    public String toString() {
        return "MetadataType{" +
                "id=" + id +
                ", path=" + path +
                ", params=" + params +
                ", def=" + def +
                ", docs=" + docs +
                '}';
    }
}
