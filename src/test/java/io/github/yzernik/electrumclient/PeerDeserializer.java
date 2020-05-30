package io.github.yzernik.electrumclient;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.yzernik.electrumclient.subscribepeers.Peer;
import io.github.yzernik.electrumclient.subscribepeers.PeerServerFeatures;

import java.io.IOException;
import java.util.List;

class PeerDeserializer extends JsonDeserializer<Peer> {
    @Override
    public Peer deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.readValueAsTree();
        String featuresListLine = node.get(2).toString();
        PeerServerFeatures peerServerFeatures = parsePeerServerFeatures(featuresListLine);
        return new Peer(node.get(0).textValue(), node.get(1).textValue(), peerServerFeatures);
    }

    private PeerServerFeatures parsePeerServerFeatures(String featuresListLine) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<String> featuresList = mapper.readValue(featuresListLine, mapper.getTypeFactory().constructCollectionType(List.class, String.class));
        return new PeerServerFeatures(featuresList);
    }
}
