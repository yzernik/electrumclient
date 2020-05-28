package io.github.yzernik.electrumclient;

public class SubscribeHeadersMessage implements ElectrumMessage {
    public String hex;
    public int height;

    @Override
    public String toString() {
        return "SubscribeHeadersMessage(" +
                "hex: " + hex + ", " +
                "height: " + height +
                ")";
    }
}
