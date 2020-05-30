package io.github.yzernik.electrumclient.subscribepeers;

import java.util.List;

public class PeerServerFeatures {
    public List<String> features;

    public PeerServerFeatures(List<String> features) {
        this.features = features;
    }

    @Override
    public String toString() {
        return "Features(" +
                String.join(", ", features) +
                ")";
    }
}
