package io.github.yzernik.electrumclient;


public class ElectrumRPCParseException extends ElectrumClientException {

    public ElectrumRPCParseException(String msg) {
        super(msg);
    }

    public ElectrumRPCParseException(Throwable e) {
        super(e);
    }

    public ElectrumRPCParseException(String msg, Throwable t) {
        super(msg, t);
    }

}