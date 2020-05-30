package io.github.yzernik.electrumclient;

import io.github.yzernik.electrumclient.exceptions.ElectrumRPCParseException;
import io.github.yzernik.electrumclient.subscribepeers.SubscribePeersResponse;

import java.io.IOException;

public class SubscribePeersClientConnection extends ElectrumClientSubscribeConnection<SubscribePeersResponse> {

    public SubscribePeersClientConnection(String host, int port, NotificationHandler<SubscribePeersResponse> notificationHandler) {
        super(host, port, notificationHandler);
    }

    @Override
    String getRPCRequest(ElectrumRPCClient electrumRPCClient) throws IOException {
        return null;
    }

    @Override
    SubscribePeersResponse parseResponseLine(String line, ElectrumRPCClient electrumRPCClient) throws ElectrumRPCParseException {
        return null;
    }

    @Override
    SubscribePeersResponse parseNotification(String line, ElectrumRPCClient electrumRPCClient) throws ElectrumRPCParseException {
        return null;
    }
}