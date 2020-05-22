package io.github.yzernik.electrumclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

abstract class ElectrumClientConnection<T extends ElectrumClientResponse> implements Runnable {

    private String host;
    private int port;
    private ThreadResult threadResult = null;

    public ElectrumClientConnection(String host, int port) {
        this.host = host;
        this.port = port;
    }


    @Override
    public void run() {
        makeRequest();
    }

    public void makeRequest() {
        try {
            InetAddress address = AddressLookup.getInetAddress(host);
            try(
                    Socket clientSocket = new Socket(address, port);
                    OutputStream clientOutputStream = clientSocket.getOutputStream();
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            ) {
                // ElectrumClientResponse response = makeRequest(clientOutputStream, in);
                T result = makeRequest(clientOutputStream, in);
                threadResult = new ThreadResult(result, null);
                // Wake up threads blocked on the getResult() method
                synchronized(this) {
                    notifyAll();
                }

                // TODO: Wait until result is closed.
                try {
                    waitForResultClose();
                } catch (IOException closeExeption) {
                    // Do nothing.
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            threadResult = new ThreadResult(null, e);
        } finally {
            synchronized(this) {
                notifyAll();
            }
        }

    }

    private void sendNewLine(OutputStream outputStream) throws IOException {
        outputStream.write("\n".getBytes());
        outputStream.flush();
    }

    public T makeRequest(OutputStream outputStream, BufferedReader in) throws IOException {
        sendRPCRequest(outputStream);
        sendNewLine(outputStream);
        return getResponse(in);
    }

    abstract void sendRPCRequest(OutputStream outputStream) throws IOException;

    abstract T getResponse(BufferedReader in) throws IOException;

    /**
     * Get the result of the connection request.
     * @return
     * @throws InterruptedException
     */
    public T getResult() throws InterruptedException, IOException {
        while (threadResult == null)
            wait();

        if (threadResult.getException() != null) {
            throw threadResult.getException();
        }

        return threadResult.getResult();
    }

    private void waitForResultClose() throws IOException {
        // TODO
        System.out.println("Waiting for result to close...");
        System.out.println("Result closed.");
    }

    public class ThreadResult {
        private T result;
        private IOException exception;

        public ThreadResult(T result, IOException exception) {
            this.result = result;
            this.exception = exception;
        }

        public T getResult() {
            return result;
        }

        public IOException getException() {
            return exception;
        }
    }

}
