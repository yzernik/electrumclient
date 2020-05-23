package io.github.yzernik.electrumclient;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ElectrumRPCClientTest {

    private static final String ID = "id";
    private static final String JSONRPC = "jsonrpc";
    private static final String METHOD = "method";
    private static final String PARAMS = "params";

    private ByteArrayOutputStream byteArrayOutputStream;
    private ElectrumRPCClient electrumRPCClient;

    @Before
    public void setUp() {
        this.byteArrayOutputStream = new ByteArrayOutputStream();
        this.electrumRPCClient = new ElectrumRPCClient(this.byteArrayOutputStream);
    }

    @After
    public void tearDown() {
        electrumRPCClient = null;
    }

    @Test
    public void testMakeRequestGetBlockHeader() throws Throwable {
        electrumRPCClient.makeRequestGetBlockHeader(27);
        String request = new String(byteArrayOutputStream.toByteArray());
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(request);

        assertTrue(jsonNode.has(ID));
        assertTrue(jsonNode.has(JSONRPC));
        assertEquals(jsonNode.get(METHOD).textValue(), "blockchain.block.header");
        assertEquals(27, jsonNode.get(PARAMS).get(0).intValue());
    }

}