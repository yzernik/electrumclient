package io.github.yzernik.electrumclient.subscribepeers;

import io.github.yzernik.electrumclient.ElectrumResponse;

import java.util.List;

public class SubscribePeersResponse implements ElectrumResponse {
    public List<Peer> peers;

    public SubscribePeersResponse(List<Peer> peers) {
        this.peers = peers;
    }

    @Override
    public String toString() {
        return "SubscribePeersResponse(" +
                String.join(", ", peers.toString()) +
                ")";
    }

}
