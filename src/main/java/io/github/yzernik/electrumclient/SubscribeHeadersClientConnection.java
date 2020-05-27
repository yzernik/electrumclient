package io.github.yzernik.electrumclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Stream;

public class SubscribeHeadersClientConnection extends ElectrumClientConnection<ElectrumClientMultiLineResponse<SubscribeHeadersResponse>> {

    // private static final int DEFAULT_SUBSCRIBE_HEADERS_SOCKET_TIMEOUT = 3000000;
    private static final int DEFAULT_SUBSCRIBE_HEADERS_SOCKET_TIMEOUT = 30000;

    public SubscribeHeadersClientConnection(String host, int port) {
        super(host, port, DEFAULT_SUBSCRIBE_HEADERS_SOCKET_TIMEOUT);
    }

    @Override
    String getRPCRequest(ElectrumRPCClient electrumRPCClient) throws IOException {
        return electrumRPCClient.makeRequestSubscribeBlockHeaders();
    }

    @Override
    ElectrumClientMultiLineResponse<SubscribeHeadersResponse> getResponse(BufferedReader in, ElectrumRPCClient electrumRPCClient) throws Throwable {
        String responseLine = in.readLine();
        System.out.println("Received initial response line: " + responseLine);
        SubscribeHeadersResponse responseItem = parseResponseLine(responseLine, electrumRPCClient);

        Stream<String> lineStream = in.lines();
        System.out.println("Got notification stream object: " + lineStream);
        Stream<SubscribeHeadersResponse> responseStream = lineStream.map(line -> {
            System.out.println("Received notification response line: " + line);
            return parseNotificationLine(line,electrumRPCClient);
        });
        Stream<SubscribeHeadersResponse> emptyStream = Stream.empty();

        return new ElectrumClientMultiLineResponse<>(responseItem, responseStream);
    }

    private SubscribeHeadersResponse parseResponseLine(String line, ElectrumRPCClient electrumRPCClient) {
        try {
            return electrumRPCClient.parseResponseSubscribeBlockHeaders(line);
        } catch (Throwable throwable) {
            return null;
        }
    }

    private SubscribeHeadersResponse parseNotificationLine(String line, ElectrumRPCClient electrumRPCClient) {
        try {
            return electrumRPCClient.parseNotificationSubscribeBlockHeaders(line);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }



}
