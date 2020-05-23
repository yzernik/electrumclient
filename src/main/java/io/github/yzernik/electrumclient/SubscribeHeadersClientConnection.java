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
        electrumRPCClient.makeRequestSubscribeBlockHeaders();
    }

    @Override
    ElectrumClientSingleLineResponse<SubscribeHeadersResponse> getResponse(BufferedReader in, ElectrumRPCClient electrumRPCClient) throws Throwable {
        SubscribeHeadersResponse subscribeHeadersResponse = electrumRPCClient.parseResponseSubscribeBlockHeaders();

        return new ElectrumClientSingleLineResponse<SubscribeHeadersResponse>(subscribeHeadersResponse);
    }

}
