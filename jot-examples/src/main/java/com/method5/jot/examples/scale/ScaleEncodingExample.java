package com.method5.jot.examples.scale;

import com.method5.jot.scale.ScaleReader;
import com.method5.jot.scale.ScaleWriter;
import com.method5.jot.util.ExampleBase;
import com.method5.jot.util.HexUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;

public class ScaleEncodingExample extends ExampleBase {
    private static final Logger logger = LoggerFactory.getLogger(ScaleEncodingExample.class);

    public static void main(String[] args) {
        execute();
    }

    private static void execute() {
        logger.info("Scale Encoding Example");

        // Encode example
        ScaleWriter scaleWriter = new ScaleWriter();
        scaleWriter.writeU32(12345);
        scaleWriter.writeCompact(BigInteger.valueOf(34232423));
        scaleWriter.writeString("test");

        byte[] encodingResult = scaleWriter.toByteArray();

        logger.info("Encoded: {}", HexUtil.bytesToHex(encodingResult));

        // Decode example
        ScaleReader scaleReader = new ScaleReader(encodingResult);
        int intValue = scaleReader.readInt();
        BigInteger compactValue = scaleReader.readCompact();
        String stringValue = scaleReader.readString();

        logger.info("Decoded: {}, {}, {}", intValue, compactValue, stringValue);
    }
}
