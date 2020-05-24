package io.github.yzernik.electrumclient;

import java.io.BufferedReader;
import java.io.IOException;

public class GetHeaderClientConnection extends ElectrumClientConnection<ElectrumClientSingleLineResponse<GetHeaderResponse>> {

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
    ElectrumClientSingleLineResponse<GetHeaderResponse> getResponse(BufferedReader in, ElectrumRPCClient electrumRPCClient) throws Throwable {
        String line = in.readLine();
        String header = electrumRPCClient.parseResponseGetBlockHeader(line);

        //String line = in.readLine();
        GetHeaderResponse getHeaderResponse = new GetHeaderResponse(header);
        return new ElectrumClientSingleLineResponse<>(getHeaderResponse);
    }

}
