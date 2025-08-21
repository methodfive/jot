package com.method5.jot.chainspec;

import com.method5.jot.TestBase;
import com.method5.jot.entity.metadata.MetadataV14;
import com.method5.jot.metadata.CallIndexResolver;
import com.method5.jot.metadata.MetadataParser;
import com.method5.jot.spec.ChainSpec;
import com.method5.jot.spec.ChainSpecBuilder;
import com.method5.jot.rpc.PolkadotRpcClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ChainSpecBuilderTest extends TestBase {
    @Test
    public void testBuildChainSpec() {
        ChainSpecBuilder chainSpecBuilder = new ChainSpecBuilder(new PolkadotRpcClient(HTTPS_DOT_RPC_SERVERS, 10000));
        ChainSpec chainSpec = chainSpecBuilder.build();

        assertNotNull(chainSpec);
        assertEquals("DOT", chainSpec.getTokenSymbol());
        assertEquals(10, chainSpec.getTokenDecimals());

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
