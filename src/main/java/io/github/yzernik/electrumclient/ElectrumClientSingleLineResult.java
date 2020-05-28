package io.github.yzernik.electrumclient;

public class ElectrumClientSingleLineResult<S extends ElectrumResponse> implements ElectrumClientResult {

    private final S line;

    public ElectrumClientSingleLineResult(S line) {
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
