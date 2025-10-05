package com.method5.jot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.method5.jot.entity.metadata.MetadataV14;
import com.method5.jot.metadata.CallIndexResolver;
import com.method5.jot.metadata.MetadataParser;
import com.method5.jot.rpc.Api;
import com.method5.jot.rpc.NopApi;
import com.method5.jot.spec.ChainSpec;
import com.method5.jot.util.HexUtil;
import org.junit.jupiter.api.BeforeAll;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestBase {
    public static final ObjectMapper objectMapper = new ObjectMapper();
    public static CallIndexResolver resolver;
    public static ChainSpec chainSpec;
    public static MetadataV14 metadata;
    public static Api api;

    public static final String[] HTTPS_DOT_RPC_SERVERS = new String[] {
            "https://polkadot-rpc.dwellir.com",
            "https://polkadot.api.onfinality.io/public-ws",
            "https://polkadot.public.curie.radiumblock.co/ws",
            "https://rpc-polkadot.luckyfriday.io" };

    public static final String[] DOT_RPC_SERVERS = new String[] {
            "wss://polkadot-rpc.dwellir.com",
            "wss://polkadot.api.onfinality.io/public-ws",
            "wss://polkadot.public.curie.radiumblock.co/ws",
            "wss://rpc-polkadot.luckyfriday.io" };

    @BeforeAll
    public static void initialize() throws IOException {
        String hex = Files.readString(Paths.get("src/test/resources/metadata.hex")).trim();
        byte[] metadataBytes = HexUtil.hexToBytes(hex);

        resolver = new CallIndexResolver();
        MetadataParser parser = new MetadataParser(resolver);

        metadata = parser.parse(metadataBytes);
        chainSpec = new ChainSpec();

        api = new NopApi(resolver);
    }
}
