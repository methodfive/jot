package com.method5.jot.examples.extrinsic;

import com.method5.jot.entity.Extrinsic;
import com.method5.jot.entity.metadata.MetadataV14;
import com.method5.jot.extrinsic.ExtrinsicDecoder;
import com.method5.jot.rpc.PolkadotRpcClient;
import com.method5.jot.util.HexUtil;
import com.method5.jot.examples.ExampleConstants;

public class ParsingExtrinsicExample {
    public static void main(String[] args) {
        MetadataV14 metadata;
        try (PolkadotRpcClient client = new PolkadotRpcClient(new String[] { ExampleConstants.RPC_SERVER }, 10000)) {
            // Retrieve current metadata
            metadata = client.getMetadata();
        }

        String extrinsicHex = "3902840054587806e52e3b3c68aeb8f4cf2f3699d058d050b1eb6f3b80f1e60ba7b5d44a01d4515d38eaf5d250a57e38deb9d9db4ddf65a333c83e60c4a18f96ab3962df6f00e513e349e68f2a6987489ad97d3d2a5a9325f27e6f7235ed5cd532bf1d73820004080005030068b48f12d74877afb8c3a3db239fa11179ab344e071b8a53a9ca2e865b29d026025a6202";

        // Parse extrinsic
        Extrinsic extrinsic = ExtrinsicDecoder.decode(HexUtil.hexToBytes(extrinsicHex), metadata);

        System.out.println(extrinsic);
    }
}
