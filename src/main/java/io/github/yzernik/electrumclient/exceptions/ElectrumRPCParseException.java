package io.github.yzernik.electrumclient.exceptions;

import io.github.yzernik.electrumclient.exceptions.ElectrumClientException;

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