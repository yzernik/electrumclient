package io.github.yzernik.electrumclient.exceptions;

public class ElectrumClientException extends Exception {

    public ElectrumClientException(String msg) {
        super(msg);
    }

    public ElectrumClientException(Throwable e) {
        super(e);
    }

    public ElectrumClientException(String msg, Throwable t) {
        super(msg, t);
    }

}