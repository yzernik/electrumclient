package io.github.yzernik.electrumclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;

public class GetHeaderClientConnection extends ElectrumClientConnection<ElectrumClientSingleLineResponse<GetHeaderResponse>> {

    public GetHeaderClientConnection(String host, int port) {
        super(host, port);
    }

    @Override
    void sendRPCRequest(OutputStream outputStream) throws IOException {
        ElectrumRPCClient electrumRPCClient = new ElectrumRPCClient(outputStream);
        electrumRPCClient.makeRequestGetBlockHeader(23);
    }

    @Override
    ElectrumClientSingleLineResponse<GetHeaderResponse> getResponse(BufferedReader in) throws IOException {
        String line = in.readLine();
        GetHeaderResponse getHeaderResponse = new GetHeaderResponse(line);
        return new ElectrumClientSingleLineResponse<>(getHeaderResponse);
    }

}
