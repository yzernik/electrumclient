package io.github.yzernik.electrumclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.stream.Stream;

public class SubscribeHeadersClientConnection extends ElectrumClientConnection<SubscribeHeadersResponse, ElectrumClientMultiLineResponse<SubscribeHeadersResponse>> {

    public SubscribeHeadersClientConnection(String host, int port) {
        super(host, port);
    }

    @Override
    void sendRPCRequest(OutputStream outputStream) throws IOException {
        ElectrumRPCClient electrumRPCClient = new ElectrumRPCClient(outputStream);
        electrumRPCClient.makeRequestSubscribeBlockHeaders();
    }

    @Override
    ElectrumClientMultiLineResponse<SubscribeHeadersResponse> getResponse(BufferedReader in) {
        Stream<String> lines = in.lines();
        // TODO: parse each response line.
        Stream<SubscribeHeadersResponse> responseStream = lines.map(line ->
                new SubscribeHeadersResponse("fooo", 77));
        return new ElectrumClientMultiLineResponse<>(responseStream);
    }

}
