package io.github.yzernik.electrumclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.jsonrpc4j.JsonRpcClient;

import java.io.*;

public class ElectrumRPCClient {

    private static final String GET_BLOCK_HEADER_REQUEST = "blockchain.block.header";
    private static final String SUBSCRIBE_BLOCK_HEADERS_REQUEST = "blockchain.headers.subscribe";

    private final JsonRpcClient client;

    public ElectrumRPCClient() {
        client = new JsonRpcClient();

        // User user = client.invoke("createUser", new Object[] { "bob", "the builder" }, User.class);
    }

    public ElectrumRPCClient(OutputStream outputStream, InputStream inputStream) {
        client = new JsonRpcClient();
        // this.outputStream = outputStream;
        // this.inputStream = inputStream;

        // User user = client.invoke("createUser", new Object[] { "bob", "the builder" }, User.class);
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

    public String parseResponseGetBlockHeader(String line) throws Throwable {
        InputStream lineInputStream = new ByteArrayInputStream(line.getBytes());
        return client.readResponse(String.class, lineInputStream);
    }

    public SubscribeHeadersResponse parseResponseSubscribeBlockHeaders(String line) throws Throwable {
        InputStream lineInputStream = new ByteArrayInputStream(line.getBytes());

        ObjectMapper mapper = new ObjectMapper();

        return client.readResponse(SubscribeHeadersResponse.class, lineInputStream);
    }

    /*
       public String parseResponseGetBlockHeader() throws Throwable {
        return client.readResponse(String.class, inputStream);
    }*/

    public String makeRequestSubscribeBlockHeaders() throws IOException {
        return makeRequestString(SUBSCRIBE_BLOCK_HEADERS_REQUEST, new Object[]{ });
    }

    /*
    public SubscribeHeadersResponse parseResponseSubscribeBlockHeaders() throws Throwable {
        ObjectMapper mapper = new ObjectMapper();

        return client.readResponse(SubscribeHeadersResponse.class, inputStream);
    }*/

}
