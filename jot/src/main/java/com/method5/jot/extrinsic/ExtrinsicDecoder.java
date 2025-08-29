package com.method5.jot.extrinsic;

import com.method5.jot.entity.metadata.MetadataV14;
import com.method5.jot.metadata.RuntimeTypeDecoder;
import com.method5.jot.scale.ScaleReader;
import com.method5.jot.entity.Extrinsic;
import com.method5.jot.entity.MultiSignature;
import com.method5.jot.entity.Mortality;
import com.method5.jot.entity.MultiAddress;

/**
 * ExtrinsicDecoder — class for extrinsic decoder in the Jot SDK. Provides key management and
 * signing; extrinsic construction and submission.
 */
public final class ExtrinsicDecoder {
    private ExtrinsicDecoder() {}

    public static Extrinsic decode(byte[] extrinsic, MetadataV14 metadata) {
        ScaleReader reader = new ScaleReader(extrinsic);

        Extrinsic result = new Extrinsic();

        reader.readCompact();

        byte version = reader.readByte();
        boolean isSigned = (version & 0x80) != 0;
        result.setVersion(version & 0x0F);

        if(result.getVersion() != 4) {
            throw new RuntimeException("Extrinsic version not supported");
        }

        result.setSigned(isSigned);

        if (isSigned) {
            result.setSigner(MultiAddress.decode(reader));
            result.setSignature(MultiSignature.decode(reader));
            result.setMortality(Mortality.decode(reader));
            result.setNonce(reader.readCompact());
            result.setTip(reader.readCompact());
            result.setMode(reader.readByte());
        }

        int module = reader.readByte();
        int function = reader.readByte();

        MetadataV14.CallInfo call = metadata.findCall(module, function);

        result.setModule(call.getPalletName());
        result.setFunction(call.getCallName());

        RuntimeTypeDecoder decoder = new RuntimeTypeDecoder(metadata.getTypes());
        for (int i = 0; i < call.getArgs().size(); i++) {
            int typeId = call.getArgTypes().get(i);
            Object value = decoder.decodeValue(typeId, reader);
            result.getArgs().add(value);
        }

        return result;
    }
}
