package com.method5.jot.examples.scale;

import com.method5.jot.scale.ScaleReader;
import com.method5.jot.scale.ScaleWriter;

import java.math.BigInteger;

public class ScaleEncodingExample {
    public static void main(String[] args) throws Exception {
        // Encode example
        ScaleWriter scaleWriter = new ScaleWriter();
        scaleWriter.writeU32(12345);
        scaleWriter.writeCompact(BigInteger.valueOf(34232423));
        scaleWriter.writeString("test");

        byte[] encodingResult = scaleWriter.toByteArray();

        // Decode example
        ScaleReader scaleReader = new ScaleReader(encodingResult);
        int intValue = scaleReader.readInt();
        BigInteger compactValue = scaleReader.readCompact();
        String stringValue = scaleReader.readString();
    }
}
