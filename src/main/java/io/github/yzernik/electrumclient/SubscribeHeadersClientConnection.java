package io.github.yzernik.electrumclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Stream;

public class SubscribeHeadersClientConnection extends ElectrumClientConnection<ElectrumClientSubscribeResponse<SubscribeHeadersMessage>> {

    private static final int DEFAULT_SUBSCRIBE_HEADERS_SOCKET_TIMEOUT = 1800000;

    public SubscribeHeadersClientConnection(String host, int port) {
        super(host, port, DEFAULT_SUBSCRIBE_HEADERS_SOCKET_TIMEOUT);
    }

    @Override
    String getRPCRequest(ElectrumRPCClient electrumRPCClient) throws IOException {
        return electrumRPCClient.makeRequestSubscribeBlockHeaders();
    }

    @Override
    ElectrumClientSubscribeResponse<SubscribeHeadersMessage> getResponse(BufferedReader in, ElectrumRPCClient electrumRPCClient) throws IOException, ElectrumRPCParseException {
        String responseLine = in.readLine();
        System.out.println("Got responseLine: " + responseLine);

        SubscribeHeadersMessage responseItem = parseResponseLine(responseLine, electrumRPCClient);
        System.out.println("Got responseItem: " + responseItem);

        Stream<String> lineStream = in.lines();
        Stream<SubscribeHeadersMessage> responseStream = lineStream.map(line -> {
            try {
                return parseNotificationLine(line,electrumRPCClient);
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        });

        return new ElectrumClientSubscribeResponse<>(responseItem, responseStream);
    }

    private SubscribeHeadersMessage parseResponseLine(String line, ElectrumRPCClient electrumRPCClient) throws ElectrumRPCParseException {
        return electrumRPCClient.parseResponseSubscribeBlockHeaders(line);
    }

    private SubscribeHeadersMessage parseNotificationLine(String line, ElectrumRPCClient electrumRPCClient) throws ElectrumRPCParseException {
        return electrumRPCClient.parseNotificationSubscribeBlockHeaders(line);
    }



}
