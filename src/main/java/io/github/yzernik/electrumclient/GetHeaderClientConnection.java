package io.github.yzernik.electrumclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;

public class GetHeaderClientConnection extends ElectrumClientConnection<ElectrumClientSingleLineResponse<GetHeaderResponse>> {

    private final int height;

    public GetHeaderClientConnection(String host, int port, int height) {
        super(host, port);
        this.height = height;
    }

    @Override
    void sendRPCRequest(OutputStream outputStream, ElectrumRPCClient electrumRPCClient) throws IOException {
        // ElectrumRPCClient electrumRPCClient = new ElectrumRPCClient(outputStream);
        electrumRPCClient.makeRequestGetBlockHeader(height);
    }

    @Override
    ElectrumClientSingleLineResponse<GetHeaderResponse> getResponse(BufferedReader in, ElectrumRPCClient electrumRPCClient) throws Throwable {
        String header = electrumRPCClient.parseResponseGetBlockHeader();

        //String line = in.readLine();
        GetHeaderResponse getHeaderResponse = new GetHeaderResponse(header);
        return new ElectrumClientSingleLineResponse<>(getHeaderResponse);
    }

}
