package io.github.yzernik.electrumclient;

import io.github.yzernik.electrumclient.exceptions.ElectrumRPCParseException;

import java.io.IOException;

public class SubscribeHeadersClientConnection extends ElectrumClientConnection<SubscribeHeadersResponse> {

    private static final int DEFAULT_SUBSCRIBE_HEADERS_SOCKET_TIMEOUT = 1800000;

    public SubscribeHeadersClientConnection(String host, int port, NotificationHandler<SubscribeHeadersResponse> notificationHandler) {
        super(host, port,notificationHandler, DEFAULT_SUBSCRIBE_HEADERS_SOCKET_TIMEOUT);
    }

    @Override
    String getRPCRequest(ElectrumRPCClient electrumRPCClient) throws IOException {
        return electrumRPCClient.makeRequestSubscribeBlockHeaders();
    }

    @Override
    SubscribeHeadersResponse parseResponseLine(String line, ElectrumRPCClient electrumRPCClient) throws ElectrumRPCParseException {
        return electrumRPCClient.parseResponseSubscribeBlockHeaders(line);
    }

    @Override
    SubscribeHeadersResponse parseNotification(String line, ElectrumRPCClient electrumRPCClient) throws ElectrumRPCParseException {
        return electrumRPCClient.parseNotificationSubscribeBlockHeaders(line);
    }

}
