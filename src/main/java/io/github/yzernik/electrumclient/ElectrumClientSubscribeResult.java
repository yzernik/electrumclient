package io.github.yzernik.electrumclient;

import java.util.stream.Stream;

public class ElectrumClientSubscribeResult<S extends ElectrumResponse> implements ElectrumClientResult {

    private final S line;
    private final Stream<S> lines;
    private boolean isComplete = false;

    public ElectrumClientSubscribeResult(S line, Stream<S> lines) {
        this.line = line;
        this.lines = lines;
        lines.onClose(this::markComplete);
    }

    public S getLine() {
        return line;
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
