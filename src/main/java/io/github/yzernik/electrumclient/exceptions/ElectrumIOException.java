package io.github.yzernik.electrumclient.exceptions;

public class ElectrumIOException extends ElectrumClientException {

    public ElectrumIOException(String msg) {
        super(msg);
    }

    public ElectrumIOException(Throwable e) {
        super(e);
    }

    public ElectrumIOException(String msg, Throwable t) {
        super(msg, t);
    }

}
