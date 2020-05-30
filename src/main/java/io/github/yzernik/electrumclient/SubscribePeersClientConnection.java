package io.github.yzernik.electrumclient;

import io.github.yzernik.electrumclient.exceptions.ElectrumRPCParseException;
import io.github.yzernik.electrumclient.subscribepeers.SubscribePeersResponse;

import java.io.IOException;

public class SubscribePeersClientConnection extends ElectrumClientConnection<SubscribePeersResponse> {

    public SubscribePeersClientConnection(String host, int port) {
        super(host, port);
    }

    @Override
    String getRPCRequest(ElectrumRPCClient electrumRPCClient) throws IOException {
        return electrumRPCClient.makeRequestSubscribePeers();
    }

    @Override
    SubscribePeersResponse parseResponseLine(String line, ElectrumRPCClient electrumRPCClient) throws ElectrumRPCParseException {
        System.out.println("Got subscribe peers response line: " + line);
        return electrumRPCClient.parseSubscribePeersResult(line);
    }

}