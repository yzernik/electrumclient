package io.github.yzernik.electrumclient;

import io.github.yzernik.electrumclient.exceptions.ElectrumRPCParseException;
import io.github.yzernik.electrumclient.subscribepeers.SubscribePeersResponse;

import java.io.IOException;
import java.net.InetSocketAddress;

public class SubscribePeersClientConnection extends ElectrumClientConnection<SubscribePeersResponse> {

    public SubscribePeersClientConnection(InetSocketAddress address) {
        super(address);
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