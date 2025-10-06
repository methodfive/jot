package com.method5.jot.rpc;

import com.method5.jot.TestBase;
import com.method5.jot.metadata.MetadataCache;
import com.method5.jot.query.ChainRpc;
import com.method5.jot.query.Query;
import com.method5.jot.query.StateRpc;
import com.method5.jot.query.SystemRpc;
import com.method5.jot.query.model.RuntimeVersion;
import com.method5.jot.query.model.SystemProperties;
import com.method5.jot.util.HexUtil;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class ApiTest extends TestBase {
    @Test
    public void testInitializeMetadataSucceeds() throws Exception {
        MetadataCache.clear();

        // Retrieves metadata on-chain
        PolkadotWs api = mock(PolkadotWs.class);
        api.isInitialized = false;
        when(api.getChainSpec()).thenReturn(chainSpec);
        when(api.getResolver()).thenReturn(resolver);
        when(api.getMetadata()).thenReturn(metadata);
        doCallRealMethod().when(api).initializeMetadata();

        SystemRpc systemRpc = mock(SystemRpc.class);
        when(systemRpc.properties()).thenReturn(new SystemProperties(1, "Test", 10));
        when(systemRpc.chain()).thenReturn("Polkadot");

        StateRpc stateRpc = mock(StateRpc.class);
        when(stateRpc.runtimeVersion()).thenReturn(new RuntimeVersion());
        when(stateRpc.metadata()).thenReturn(HexUtil.hexToBytes(metadataHex));
        ChainRpc chainRpc = mock(ChainRpc.class);
        when(chainRpc.genesisBlockHash()).thenReturn("00");

        Query query = mock(Query.class);
        when(query.chain()).thenReturn(chainRpc);
        when(query.state()).thenReturn(stateRpc);
        when(query.system()).thenReturn(systemRpc);
        when(api.query()).thenReturn(query);

        // Retrieves on-chain and saves to cache
        api.initializeMetadata();

        api.isInitialized = false;

        // Retrieves from cache. Will throw exception if cache is not set
        api.initializeMetadata();

        try (OfflineApi api2 = new OfflineApi()) {
            // Retrieves from cache. Will throw exception if cache is not set
            api2.initializeMetadata();
        }
    }}
