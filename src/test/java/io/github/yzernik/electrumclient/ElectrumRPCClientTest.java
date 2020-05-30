package io.github.yzernik.electrumclient;


import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.github.yzernik.electrumclient.subscribepeers.SubscribePeersResponse;
import io.github.yzernik.electrumclient.subscribepeers.SubscribePeersResponseDeserializer;
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

/*
    @Test
    public void testParseSubscribePeersResponse() throws Throwable {
        String peersResultLine = "[ [ \"83.212.111.114\", \"electrum.stepkrav.pw\", [ \"v0.9\", \"p100\", \"t\", \"h\", \"s\", \"g\" ] ], [ \"23.94.27.149\", \"ultra-feather.net\", [ \"v0.9\", \"p10000\", \"t\", \"h\", \"s\", \"g\" ] ], [ \"88.198.241.196\", \"electrum.be\", [ \"v0.9\", \"p10000\", \"t\", \"h\", \"s\", \"g\" ] ] ]";
        // String peersResultLine = "[]";

        ObjectMapper mapper = new ObjectMapper();
        SimpleModule customModule = new SimpleModule("SubscribePeersModule", new Version(0, 1, 0, null));
        customModule.addDeserializer(Peer.class, new PeerDeserializer());
        // customModule.addDeserializer(PeerServerFeatures.class, new PeerServerFeaturesDeserializer());
        mapper.registerModule(customModule);

        List<Peer> deserializedList = mapper.readValue(peersResultLine, mapper.getTypeFactory().constructCollectionType(List.class, Peer.class));
        assertEquals(deserializedList.size(), 3);
        assertEquals(deserializedList.get(0).ip, "83.212.111.114");
        assertEquals(deserializedList.get(0).hostname, "electrum.stepkrav.pw");
        assertEquals(deserializedList.get(0).features.features.get(0), "v0.9");

        System.out.println(deserializedList.get(0));

        // SubscribePeersResponse response = electrumRPCClient.parseSubscribePeersResult(peersResultLine);
    }
*/

    @Test
    public void testParseSubscribePeersResponseWithMapper() throws Throwable {
        String peersResultLine = "[ [ \"83.212.111.114\", \"electrum.stepkrav.pw\", [ \"v0.9\", \"p100\", \"t\", \"h\", \"s\", \"g\" ] ], [ \"23.94.27.149\", \"ultra-feather.net\", [ \"v0.9\", \"p10000\", \"t\", \"h\", \"s\", \"g\" ] ], [ \"88.198.241.196\", \"electrum.be\", [ \"v0.9\", \"p10000\", \"t\", \"h\", \"s\", \"g\" ] ] ]";
        // String peersResultLine = "[]";

        ObjectMapper mapper = new ObjectMapper();
        SimpleModule customModule = new SimpleModule("SubscribePeersModule", new Version(0, 1, 0, null));
        customModule.addDeserializer(SubscribePeersResponse.class, new SubscribePeersResponseDeserializer());
        mapper.registerModule(customModule);

        SubscribePeersResponse response = mapper.readValue(peersResultLine, SubscribePeersResponse.class);

        System.out.println(response);

        assertEquals(response.peers.size(), 3);
        assertEquals(response.peers.get(0).ip, "83.212.111.114");
        assertEquals(response.peers.get(0).hostname, "electrum.stepkrav.pw");
        assertEquals(response.peers.get(0).features.features.get(0), "v0.9");
    }

}