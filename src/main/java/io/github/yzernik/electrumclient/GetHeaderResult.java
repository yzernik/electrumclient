package io.github.yzernik.electrumclient;

public class GetHeaderResult extends ElectrumClientSingleLineResult<GetHeaderResponse> {

    public GetHeaderResult(GetHeaderResponse line) {
        super(line);
    }

}
