package io.github.yzernik.electrumclient;

public class GetHeaderResponse extends ElectrumResponse {

    public GetHeaderResponse(String hex) {
        this.hex = hex;
    }

    public String hex;

}
