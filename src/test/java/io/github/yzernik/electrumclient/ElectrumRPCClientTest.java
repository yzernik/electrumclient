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
        this.electrumRPCClient = new ElectrumRPCClient();
    }

    @After
    public void tearDown() {
        electrumRPCClient = null;
    }

    @Test
    public void testMakeRequestGetBlockHeader() throws Throwable {
        String request = electrumRPCClient.makeRequestGetBlockHeader(27);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(request);

        assertTrue(jsonNode.has(ID));
        assertTrue(jsonNode.has(JSONRPC));
        assertEquals(jsonNode.get(METHOD).textValue(), "blockchain.block.header");
        assertEquals(27, jsonNode.get(PARAMS).get(0).intValue());
    }

    @Test
    public void testParseNotificationSubscribeBlockHeaders() throws Throwable {
        String notificationLine = " {\"jsonrpc\": \"2.0\", \"method\": \"blockchain.headers.subscribe\", \"params\": [{\"hex\": \"00e0ff2730474ed1def5b826e9ffcce8d8c61415fefd398b6c170a000000000000000000f63517f98c86cf28d508f32d2f8aea2e9f8d2a36fdb904198420d40e8c19896660b6c85ef6971217d8193d7b\", \"height\": 631384}]}";
        SubscribeHeadersResponse response = electrumRPCClient.parseNotificationSubscribeBlockHeaders(notificationLine);

        assertEquals(631384, response.height);
        assertEquals(631384, response.height);
        assertEquals("00e0ff2730474ed1def5b826e9ffcce8d8c61415fefd398b6c170a000000000000000000f63517f98c86cf28d508f32d2f8aea2e9f8d2a36fdb904198420d40e8c19896660b6c85ef6971217d8193d7b", response.hex);
    }

}