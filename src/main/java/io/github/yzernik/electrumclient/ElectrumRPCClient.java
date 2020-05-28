package io.github.yzernik.electrumclient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.jsonrpc4j.JsonRpcClient;

import java.io.*;

public class ElectrumRPCClient {

    private static final String GET_BLOCK_HEADER_REQUEST = "blockchain.block.header";
    private static final String SUBSCRIBE_BLOCK_HEADERS_REQUEST = "blockchain.headers.subscribe";

    private final JsonRpcClient client;

    public ElectrumRPCClient() {
        client = new JsonRpcClient();
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

}
