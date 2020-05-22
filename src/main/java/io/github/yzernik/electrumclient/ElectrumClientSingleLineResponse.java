package io.github.yzernik.electrumclient;

public class ElectrumClientSingleLineResponse<S extends ElectrumResponse> implements ElectrumClientResponse<S> {

    private final S line;

    public ElectrumClientSingleLineResponse(S line) {
        this.line = line;
    }

    public S getLine() {
        return line;
    }

    @Override
    public void waitUntilComplete() {
        return;
    }
}
