package io.github.yzernik.electrumclient;

import io.github.yzernik.electrumclient.exceptions.ElectrumRPCParseException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class SubscribeHeadersClientConnection extends ElectrumClientSubscribeConnection<SubscribeHeadersResponse> {

    private static final int DEFAULT_SUBSCRIBE_HEADERS_SOCKET_TIMEOUT = 1800000;

    public SubscribeHeadersClientConnection(InetSocketAddress address, NotificationHandler<SubscribeHeadersResponse> notificationHandler) {
        super(address, notificationHandler, DEFAULT_SUBSCRIBE_HEADERS_SOCKET_TIMEOUT);
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
