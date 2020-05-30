package io.github.yzernik.electrumclient.subscribepeers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SubscribePeersResponseDeserializer extends JsonDeserializer<SubscribePeersResponse> {
    @Override
    public SubscribePeersResponse deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.readValueAsTree();
        List<Peer> peers = new ArrayList<>();
        for (int i = 0; i < node.size(); i++) {
            String peerLine = node.get(i).toString();
            Peer peer = parsePeer(peerLine);
            peers.add(peer);
        }
        return new SubscribePeersResponse(peers);
    }

    private Peer parsePeer(String peerLine) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule customModule = new SimpleModule("SubscribePeersModule", new Version(0, 1, 0, null));
        customModule.addDeserializer(Peer.class, new PeerDeserializer());
        mapper.registerModule(customModule);
        return mapper.readValue(peerLine, Peer.class);
    }

}
