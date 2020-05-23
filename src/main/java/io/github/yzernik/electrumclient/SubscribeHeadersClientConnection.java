package io.github.yzernik.electrumclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.stream.Stream;

public class SubscribeHeadersClientConnection extends ElectrumClientConnection<ElectrumClientSingleLineResponse<SubscribeHeadersResponse>> {

    public SubscribeHeadersClientConnection(String host, int port) {
        super(host, port);
    }

    @Override
    void sendRPCRequest(OutputStream outputStream, ElectrumRPCClient electrumRPCClient) throws IOException {
        // ElectrumRPCClient electrumRPCClient = new ElectrumRPCClient(outputStream);
        String requestBlocksRequestString = electrumRPCClient.makeRequestSubscribeBlockHeaders();
        outputStream.write(requestBlocksRequestString.getBytes());
    }

    @Override
    ElectrumClientSingleLineResponse<SubscribeHeadersResponse> getResponse(BufferedReader in, ElectrumRPCClient electrumRPCClient) throws Throwable {
        String line = in.readLine();
        SubscribeHeadersResponse subscribeHeadersResponse = electrumRPCClient.parseResponseSubscribeBlockHeaders(line);

        return new ElectrumClientSingleLineResponse<>(subscribeHeadersResponse);
    }

}
