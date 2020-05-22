package io.github.yzernik.electrumclient;

public class SubscribeHeadersResponse implements ElectrumResponse{

    public SubscribeHeadersResponse(String hex, int height) {
        this.hex = hex;
        this.height = height;
    }

    public String hex;

    public int height;

}
