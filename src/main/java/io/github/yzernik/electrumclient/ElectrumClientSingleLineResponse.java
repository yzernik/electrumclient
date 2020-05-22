package io.github.yzernik.electrumclient;

public class ElectrumClientSingleLineResponse implements ElectrumClientResponse {

    private String line;

    public ElectrumClientSingleLineResponse(String line) {
        this.line = line;
    }

    public String getLine() {
        return line;
    }
}
