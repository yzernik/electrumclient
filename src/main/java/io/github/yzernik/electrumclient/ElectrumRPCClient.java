package io.github.yzernik.electrumclient;

import com.googlecode.jsonrpc4j.JsonRpcClient;

import java.io.IOException;
import java.io.OutputStream;

public class ElectrumRPCClient {

    private static final String GET_BLOCK_HEADER_REQUEST = "blockchain.block.header";
    private static final String SUBSCRIBE_BLOCK_HEADERS_REQUEST = "blockchain.headers.subscribe";

    private JsonRpcClient client;
    private OutputStream outputStream;

    public ElectrumRPCClient(OutputStream outputStream) {
        client = new JsonRpcClient();
        this.outputStream = outputStream;

        // User user = client.invoke("createUser", new Object[] { "bob", "the builder" }, User.class);
    }

    public void makeRequestGetBlockHeader(int height) throws IOException {
        client.invoke(GET_BLOCK_HEADER_REQUEST, new Object[]{ height }, outputStream);
    }

    public void makeRequestSubscribeBlockHeaders() throws IOException {
        client.invoke(SUBSCRIBE_BLOCK_HEADERS_REQUEST, new Object[]{ }, outputStream);
    }

}
