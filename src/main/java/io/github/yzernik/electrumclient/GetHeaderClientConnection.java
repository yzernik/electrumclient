package io.github.yzernik.electrumclient;

import java.io.IOException;
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
    void sendRPCRequest() throws IOException {
        ElectrumRPCClient electrumRPCClient = new ElectrumRPCClient(getClientOutputStream());
        electrumRPCClient.makeRequestGetBlockHeader(23);
    }

    @Override
    ElectrumClientSingleLineResponse getResponse() throws IOException {
        String line = getIn().readLine();
        return new ElectrumClientSingleLineResponse(line);
    }
}
