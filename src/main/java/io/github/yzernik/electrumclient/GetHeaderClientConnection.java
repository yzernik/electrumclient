package io.github.yzernik.electrumclient;

import java.io.BufferedReader;
import java.io.IOException;

public class GetHeaderClientConnection extends ElectrumClientConnection<ElectrumClientSingleLineResponse<GetHeaderMessage>> {

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
    ElectrumClientSingleLineResponse<GetHeaderMessage> getResponse(BufferedReader in, ElectrumRPCClient electrumRPCClient) throws IOException, ElectrumRPCParseException {
        String line = in.readLine();
        String header = electrumRPCClient.parseResponseGetBlockHeader(line);

        GetHeaderMessage getHeaderResponse = new GetHeaderMessage(header);
        return new ElectrumClientSingleLineResponse<>(getHeaderResponse);
    }

}
