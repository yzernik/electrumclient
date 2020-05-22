package io.github.yzernik.electrumclient;

import java.util.stream.Stream;

public class ElectrumClientMultiLineResponse implements ElectrumClientResponse{

    private final Stream<String> lines;
    private boolean isComplete = false;

    public ElectrumClientMultiLineResponse(Stream<String> lines) {
        this.lines = lines;
        lines.onClose(() -> {
            markComplete();
        });
    }

    public Stream<String> getLines() {
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
                System.err.println("Thread interrupted: " + e);
            }
        }
        return;
    }

    public synchronized void markComplete() {
        isComplete = true;
        notifyAll();
    }
}
