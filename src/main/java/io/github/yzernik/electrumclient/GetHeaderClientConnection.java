package io.github.yzernik.electrumclient;

import io.github.yzernik.electrumclient.exceptions.ElectrumRPCParseException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class GetHeaderClientConnection extends ElectrumClientConnection<GetHeaderResponse> {

    private final int height;

    public GetHeaderClientConnection(InetSocketAddress address, int height) {
        super(address);
        this.height = height;
    }

    @Override
    String getRPCRequest(ElectrumRPCClient electrumRPCClient) throws IOException {
        return electrumRPCClient.makeRequestGetBlockHeader(height);
    }

    @Override
    GetHeaderResponse parseResponseLine(String line, ElectrumRPCClient electrumRPCClient) throws ElectrumRPCParseException {
        String hex = electrumRPCClient.parseResponseGetBlockHeader(line);
        return new GetHeaderResponse(hex);
    }

}
