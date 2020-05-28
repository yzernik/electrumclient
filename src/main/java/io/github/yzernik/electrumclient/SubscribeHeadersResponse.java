package io.github.yzernik.electrumclient;

public class SubscribeHeadersResponse implements ElectrumResponse {
    public String hex;
    public int height;

    @Override
    public String toString() {
        return "SubscribeHeadersResponse(" +
                "hex: " + hex + ", " +
                "height: " + height +
                ")";
    }
}
