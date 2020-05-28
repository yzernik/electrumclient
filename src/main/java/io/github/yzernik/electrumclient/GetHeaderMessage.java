package io.github.yzernik.electrumclient;

public class GetHeaderMessage implements ElectrumMessage {

    public GetHeaderMessage(String hex) {
        this.hex = hex;
    }

    public String hex;

}
