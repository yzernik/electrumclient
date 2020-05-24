package io.github.yzernik.electrumclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.stream.Stream;

public class SubscribeHeadersClientConnection extends ElectrumClientConnection<ElectrumClientMultiLineResponse<SubscribeHeadersResponse>> {

    public SubscribeHeadersClientConnection(String host, int port) {
        super(host, port);
    }

    @Override
    void sendRPCRequest(OutputStream outputStream, ElectrumRPCClient electrumRPCClient) throws IOException {
        // ElectrumRPCClient electrumRPCClient = new ElectrumRPCClient(outputStream);
        String requestBlocksRequestString = electrumRPCClient.makeRequestSubscribeBlockHeaders();
        outputStream.write(requestBlocksRequestString.getBytes());
    }

    @Override
    ElectrumClientMultiLineResponse<SubscribeHeadersResponse> getResponse(BufferedReader in, ElectrumRPCClient electrumRPCClient) throws Throwable {
        Stream<String> lineStream = in.lines();
        Stream<SubscribeHeadersResponse> responseStream = lineStream.map(line -> {
            System.out.println("Parsing line: " + line);
            return parseResponseLine(line,electrumRPCClient);
        });
        return new ElectrumClientMultiLineResponse<>(responseStream);
    }

    private SubscribeHeadersResponse parseResponseLine(String line, ElectrumRPCClient electrumRPCClient) {
        try {
            return electrumRPCClient.parseResponseSubscribeBlockHeaders(line);
        } catch (Throwable throwable) {
            return null;
        }
    }

}
