package io.github.yzernik.electrumclient;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.googlecode.jsonrpc4j.JsonRpcClient;
import io.github.yzernik.electrumclient.exceptions.ElectrumRPCParseException;
import io.github.yzernik.electrumclient.subscribepeers.SubscribePeersResponse;
import io.github.yzernik.electrumclient.subscribepeers.SubscribePeersResponseDeserializer;

import java.io.*;

public class ElectrumRPCClient {

    private static final String GET_BLOCK_HEADER_REQUEST = "blockchain.block.header";
    private static final String SUBSCRIBE_BLOCK_HEADERS_REQUEST = "blockchain.headers.subscribe";
    private static final String SUBSCRIBE_PEERS_REQUEST = "server.peers.subscribe";

    private final JsonRpcClient client;

    public ElectrumRPCClient() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule customModule = new SimpleModule("SubscribePeersModule", new Version(0, 1, 0, null));
        customModule.addDeserializer(SubscribePeersResponse.class, new SubscribePeersResponseDeserializer());
        mapper.registerModule(customModule);

        client = new JsonRpcClient(mapper);
    }

    private String makeRequestString(String methodName, Object argument) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        client.invoke(methodName, argument, baos);
        return baos.toString();
    }

    public String makeRequestGetBlockHeader(int height) throws IOException {
        // client.invoke(GET_BLOCK_HEADER_REQUEST, new Object[]{ height }, outputStream);
        return makeRequestString(GET_BLOCK_HEADER_REQUEST, new Object[]{ height });
    }

    public String parseResponseGetBlockHeader(String line) throws ElectrumRPCParseException {
        try {
            InputStream lineInputStream = new ByteArrayInputStream(line.getBytes());
            return client.readResponse(String.class, lineInputStream);
        } catch (Throwable throwable) {
            throw new ElectrumRPCParseException(throwable);
        }
    }

    public SubscribeHeadersResponse parseResponseSubscribeBlockHeaders(String line) throws ElectrumRPCParseException {
        InputStream lineInputStream = new ByteArrayInputStream(line.getBytes());

        ObjectMapper mapper = new ObjectMapper();

        try {
            return client.readResponse(SubscribeHeadersResponse.class, lineInputStream);
        } catch (Throwable throwable) {
            throw new ElectrumRPCParseException(throwable);
        }
    }

    public String makeRequestSubscribeBlockHeaders() throws IOException {
        return makeRequestString(SUBSCRIBE_BLOCK_HEADERS_REQUEST, new Object[]{ });
    }

    public SubscribeHeadersResponse parseNotificationSubscribeBlockHeaders(String line) throws ElectrumRPCParseException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(line);
            String paramsLine = jsonNode.get("params").get(0).toString();
            return mapper.readValue(paramsLine, SubscribeHeadersResponse.class);
        } catch (IOException e) {
            throw new ElectrumRPCParseException(e);
        }
    }

    public SubscribePeersResponse parseSubscribePeersResult(String line) throws ElectrumRPCParseException {
        InputStream lineInputStream = new ByteArrayInputStream(line.getBytes());
        try {
            return client.readResponse(SubscribePeersResponse.class, lineInputStream);
        } catch (Throwable throwable) {
            throw new ElectrumRPCParseException(throwable);
        }
    }


    public String makeRequestSubscribePeers() throws IOException {
        return makeRequestString(SUBSCRIBE_PEERS_REQUEST, new Object[]{ });
    }

}
