package io.github.yzernik.electrumclient;

public class GetHeaderResponse implements ElectrumResponse {

    public GetHeaderResponse(String hex) {
        this.hex = hex;
    }

    public String hex;

    @Override
    public String toString() {
        return "GetHeaderResponse(" +
                "hex: " + hex +
                ")";
    }
}
