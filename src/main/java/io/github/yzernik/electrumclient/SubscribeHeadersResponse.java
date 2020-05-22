package io.github.yzernik.electrumclient;

public class SubscribeHeadersResponse {

    public SubscribeHeadersResponse(String hex, int height) {
        this.hex = hex;
        this.height = height;
    }

    public String hex;

    public int height;

}
