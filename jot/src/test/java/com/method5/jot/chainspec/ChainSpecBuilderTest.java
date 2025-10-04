package com.method5.jot.chainspec;

import com.method5.jot.TestBase;
import com.method5.jot.entity.metadata.MetadataV14;
import com.method5.jot.metadata.CallIndexResolver;
import com.method5.jot.metadata.MetadataParser;
import com.method5.jot.spec.ChainSpec;
import com.method5.jot.spec.ChainSpecBuilder;
import com.method5.jot.rpc.PolkadotRpcClient;
import com.method5.jot.util.HexUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

public class ChainSpecBuilderTest extends TestBase {
    private static final Logger logger = LoggerFactory.getLogger(ChainSpecBuilderTest.class);

    @Test
    public void testBuildChainSpec() {
        ChainSpecBuilder chainSpecBuilder = new ChainSpecBuilder(new PolkadotRpcClient(HTTPS_DOT_RPC_SERVERS, 10000));
        ChainSpec chainSpec = chainSpecBuilder.build();

        assertNotNull(chainSpec);
        assertEquals("DOT", chainSpec.getTokenSymbol());
        assertEquals(10, chainSpec.getTokenDecimals());
        assertEquals(26, chainSpec.getTransactionVersion());
        assertEquals("91b171bb158e2d3848fa23a9f1c25182fb8e20313b2c1eb49219da7a70ce90c3", HexUtil.bytesToHex(chainSpec.getGenesisHash()));
        assertEquals(0, chainSpec.getSs58Prefix());
        assertEquals("Polkadot", chainSpec.getId());
        assertEquals("Parity Polkadot", chainSpec.getName());
        assertEquals(1007001, chainSpec.getSpecVersion());

        logger.info(chainSpec.toString());

        CallIndexResolver resolver = new CallIndexResolver();
        MetadataParser parser = new MetadataParser(resolver);

        MetadataV14 metadata = parser.parse(chainSpec.getMetadata());
        assertNotNull(metadata);
    }

    @Test
    public void testMissingRpcServer() {
        assertThrows(RuntimeException.class, () -> {
            ChainSpecBuilder chainSpecBuilder = new ChainSpecBuilder(null);
            chainSpecBuilder.build();
        });
    }
}
