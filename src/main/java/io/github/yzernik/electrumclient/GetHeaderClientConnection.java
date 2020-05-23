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
        String getBlockHeaderRequestString = electrumRPCClient.makeRequestGetBlockHeader(height);
        outputStream.write(getBlockHeaderRequestString.getBytes());
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
