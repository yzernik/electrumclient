package io.github.yzernik.electrumclient;

import java.util.stream.Stream;

public class ElectrumClientMultiLineResponse implements ElectrumClientResponse{

    private Stream<String> lines;

    public ElectrumClientMultiLineResponse(Stream<String> lines) {
        this.lines = lines;
    }

    public Stream<String> getLines() {
        return lines;
    }

}
