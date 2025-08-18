package com.method5.jot.entity.metadata;

import com.method5.jot.scale.ScaleReader;
import com.method5.jot.scale.ScaleWriter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class MetadataPallet {
    private String name;
    private int index;
    private PalletStorage storage;
    private Call call;
    private Event event;
    private Error errors;
    private List<PalletConstant> constants;

    public MetadataPallet() {
        constants = new ArrayList<>();
    }

    public static MetadataPallet decode(ScaleReader reader) {
        MetadataPallet p = new MetadataPallet();
        p.setName(reader.readString());

        if(reader.readBoolean()) {
            p.setStorage(PalletStorage.decode(reader));
        }

        if(reader.readBoolean()) {
            p.setCall(Call.decode(reader));
        }

        if(reader.readBoolean()) {
            p.setEvent(Event.decode(reader));
        }

        int constantsLen = reader.readCompact().intValue();
        for(int i = 0; i < constantsLen; i++) {
            p.getConstants().add(PalletConstant.decode(reader));
        }

        if(reader.readBoolean()) {
            p.setErrors(Error.decode(reader));
        }

        p.setIndex(reader.readByte());
        return p;
    }

    public byte[] encode() {
        ScaleWriter scaleWriter = new ScaleWriter();
        scaleWriter.writeString(getName());

        scaleWriter.writeOptional(getStorage(), PalletStorage::encode);
        scaleWriter.writeOptional(getCall(), Call::encode);
        scaleWriter.writeOptional(getEvent(), Event::encode);

        scaleWriter.writeCompact(BigInteger.valueOf(getConstants().size()));
        for(PalletConstant constant : getConstants()) {
            scaleWriter.writeBytes(constant.encode());
        }

        scaleWriter.writeOptional(getErrors(), Error::encode);
        scaleWriter.writeByte(getIndex());

        return scaleWriter.toByteArray();
    }

    public static class Call {
        private int type;

        public static Call decode(ScaleReader reader) {
            Call call = new Call();
            call.setType(reader.readCompact().intValue());
            return call;
        }

        public byte[] encode() {
            ScaleWriter writer = new ScaleWriter();
            writer.writeCompact(BigInteger.valueOf(getType()));
            return writer.toByteArray();
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return "Call{" +
                    "type=" + type +
                    '}';
        }
    }

    public static class Event {
        private int type;

        public static Event decode(ScaleReader reader) {
            Event event = new Event();
            event.setType(reader.readCompact().intValue());
            return event;
        }

        public byte[] encode() {
            ScaleWriter writer = new ScaleWriter();
            writer.writeCompact(BigInteger.valueOf(getType()));
            return writer.toByteArray();
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return "Event{" +
                    "type=" + type +
                    '}';
        }
    }

    public static class Error {
        private int type;

        public static Error decode(ScaleReader reader) {
            Error error = new Error();
            error.setType(reader.readCompact().intValue());
            return error;
        }

        public byte[] encode() {
            ScaleWriter writer = new ScaleWriter();
            writer.writeCompact(BigInteger.valueOf(getType()));
            return writer.toByteArray();
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return "Error{" +
                    "type=" + type +
                    '}';
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public PalletStorage getStorage() {
        return storage;
    }

    public void setStorage(PalletStorage storage) {
        this.storage = storage;
    }

    public Call getCall() {
        return call;
    }

    public void setCall(Call call) {
        this.call = call;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Error getErrors() {
        return errors;
    }

    public void setErrors(Error errors) {
        this.errors = errors;
    }

    public List<PalletConstant> getConstants() {
        return constants;
    }

    public void setConstants(List<PalletConstant> constants) {
        this.constants = constants;
    }

    @Override
    public String toString() {
        return "MetadataPallet{" +
                "name='" + name + '\'' +
                ", index=" + index +
                ", storage=" + storage +
                ", call=" + call +
                ", event=" + event +
                ", errors=" + errors +
                ", constants=" + constants +
                '}';
    }
}
