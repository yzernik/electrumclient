package io.github.yzernik.electrumclient;

import java.util.stream.Stream;

public class ElectrumClientMultiLineResponse<S extends ElectrumResponse> implements ElectrumClientResponse<S> {

    private final Stream<S> lines;
    private boolean isComplete = false;

    public ElectrumClientMultiLineResponse(Stream<S> lines) {
        this.lines = lines;
        lines.onClose(this::markComplete);
    }

    public Stream<S> getLines() {
        return lines;
    }

    @Override
    public void waitUntilComplete() {
        waitForComplete();
    }

    public synchronized void waitForComplete() {
        while (!isComplete) {
            try {
                wait();
            } catch (InterruptedException e)  {
                Thread.currentThread().interrupt();
            }
        }
        return;
    }

    public synchronized void markComplete() {
        isComplete = true;
        notifyAll();
    }
}
