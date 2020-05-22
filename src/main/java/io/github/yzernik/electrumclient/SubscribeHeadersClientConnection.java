package io.github.yzernik.electrumclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.stream.Stream;

public class SubscribeHeadersClientConnection extends ElectrumClientConnection {

    public SubscribeHeadersClientConnection(String host, int port) throws UnknownHostException {
        super(host, port);
    }

    public SubscribeHeadersClientConnection(InetAddress address, int port) {
        super(address, port);
    }

    @Override
    void sendRPCRequest(OutputStream outputStream) throws IOException {
        ElectrumRPCClient electrumRPCClient = new ElectrumRPCClient(outputStream);
        electrumRPCClient.makeRequestSubscribeBlockHeaders();
    }

    @Override
    ElectrumClientResponse getResponse(BufferedReader in) throws IOException {
        Stream<String> lines = in.lines();
        return new ElectrumClientMultiLineResponse(lines);
    }
}
