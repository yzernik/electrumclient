package io.github.yzernik.electrumclient;

public interface ElectrumClientResponse<S extends ElectrumResponse> {

    void waitUntilComplete();

}
