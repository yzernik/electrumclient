package io.github.yzernik.electrumclient;

import java.io.IOException;

public class GetHeaderClientConnection extends ElectrumClientConnection<GetHeaderResponse> {

    private final int height;

    public GetHeaderClientConnection(String host, int port, int height) {
        super(host, port);
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

    @Override
    GetHeaderResponse parseNotification(String line, ElectrumRPCClient electrumRPCClient) throws ElectrumRPCParseException {
        return null;
    }

}
