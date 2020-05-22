package io.github.yzernik.electrumclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class GetHeaderClientConnection extends ElectrumClientConnection<ElectrumClientSingleLineResponse> {

    public GetHeaderClientConnection(String host, int port) throws UnknownHostException {
        super(host, port);
    }

    public GetHeaderClientConnection(InetAddress address, int port) {
        super(address, port);
    }

    @Override
    void sendRPCRequest(OutputStream outputStream) throws IOException {
        ElectrumRPCClient electrumRPCClient = new ElectrumRPCClient(outputStream);
        electrumRPCClient.makeRequestGetBlockHeader(23);
    }

    @Override
    ElectrumClientSingleLineResponse getResponse(BufferedReader in) throws IOException {
        String line = in.readLine();
        return new ElectrumClientSingleLineResponse(line);
    }

}
