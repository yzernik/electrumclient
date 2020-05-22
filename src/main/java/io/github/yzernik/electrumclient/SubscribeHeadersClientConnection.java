package io.github.yzernik.electrumclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.stream.Stream;

public class SubscribeHeadersClientConnection extends ElectrumClientConnection<ElectrumClientMultiLineResponse> {

    public SubscribeHeadersClientConnection(String host, int port) {
        super(host, port);
    }

    @Override
    void sendRPCRequest(OutputStream outputStream) throws IOException {
        ElectrumRPCClient electrumRPCClient = new ElectrumRPCClient(outputStream);
        electrumRPCClient.makeRequestSubscribeBlockHeaders();
    }

    @Override
    ElectrumClientMultiLineResponse getResponse(BufferedReader in) throws IOException {
        Stream<String> lines = in.lines();
        return new ElectrumClientMultiLineResponse(lines);
    }
}
