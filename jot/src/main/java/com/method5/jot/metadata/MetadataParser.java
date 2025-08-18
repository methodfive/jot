
package com.method5.jot.metadata;

import com.method5.jot.entity.metadata.MetadataV14;

public class MetadataParser {
    private final CallIndexResolver resolver;

    public MetadataParser(CallIndexResolver resolver) {
        this.resolver = resolver;
    }

    public MetadataV14 parse(byte[] metadataBytes) {
        MetadataV14 metadata = MetadataV14.decode(metadataBytes);
        resolver.initialize(metadata);
        return metadata;
    }
}
