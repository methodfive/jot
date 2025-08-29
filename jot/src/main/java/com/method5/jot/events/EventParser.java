package com.method5.jot.events;

import com.method5.jot.entity.Phase;
import com.method5.jot.entity.metadata.MetadataTypeField;
import com.method5.jot.entity.metadata.MetadataV14;
import com.method5.jot.metadata.CallIndexResolver;
import com.method5.jot.metadata.RuntimeTypeDecoder;
import com.method5.jot.scale.ScaleReader;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * EventParser — class for event parser in the Jot SDK. Provides key management and signing; event
 * parsing and dispatch errors.
 */
public class EventParser {
    public static List<EventRecord> parse(byte[] raw, MetadataV14 metadata, CallIndexResolver callIndexResolver) {
        ScaleReader reader = new ScaleReader(raw);
        BigInteger count = reader.readCompact();

        List<EventRecord> events = new ArrayList<>();

        for (int i = 0; i < count.intValue(); i++) {
            Phase phase = Phase.decode(reader);

            int palletIndex = Byte.toUnsignedInt(reader.readByte());
            int eventIndex = Byte.toUnsignedInt(reader.readByte());

            String pallet = callIndexResolver.getModuleName(palletIndex);
            String method = callIndexResolver.getEventName(palletIndex, eventIndex);

            RuntimeTypeDecoder decoder = new RuntimeTypeDecoder(metadata.getTypes());
            List<MetadataTypeField> fields = metadata.getEventFields(palletIndex, eventIndex);
            Map<String, RuntimeTypeDecoder.TypeAndValue> attributes = new HashMap<>();
            for(MetadataTypeField field : fields) {
                Object value = decoder.decodeValue(field.getType(), reader);
                attributes.put(field.getTypeName(), new RuntimeTypeDecoder.TypeAndValue(field.getType(), value));
            }

            BigInteger topicsCount = reader.readCompact();
            for (int t = 0; t < topicsCount.intValue(); t++) {
                reader.readBytes(32); // skip each topic
            }

            events.add(new EventRecord(phase, pallet, method, attributes));
        }

        return events;
    }
}

