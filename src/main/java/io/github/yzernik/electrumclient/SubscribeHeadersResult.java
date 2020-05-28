package io.github.yzernik.electrumclient;

import java.util.stream.Stream;

public class SubscribeHeadersResult extends ElectrumClientSubscribeResult<SubscribeHeadersResponse> {

    public SubscribeHeadersResult(SubscribeHeadersResponse line, Stream<SubscribeHeadersResponse> lines) {
        super(line, lines);
    }

}
