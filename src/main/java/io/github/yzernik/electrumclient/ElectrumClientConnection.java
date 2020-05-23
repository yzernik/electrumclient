package io.github.yzernik.electrumclient;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

abstract class ElectrumClientConnection<T extends ElectrumClientResponse> implements Runnable {

    private final String host;
    private final int port;
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
                    InputStream clientInputStream = clientSocket.getInputStream();
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientInputStream))
            ) {
                ElectrumRPCClient electrumRPCClient = new ElectrumRPCClient(clientOutputStream, clientInputStream);
                T result = makeRequest(clientOutputStream, in, electrumRPCClient);
                threadResult = new ThreadResult(result, null);

                // Wake up threads blocked on the getResult() method
                synchronized(this) {
                    notifyAll();
                }

                waitForResultClose(result);

            }
        } catch (Throwable e) {
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

    public T makeRequest(OutputStream outputStream, BufferedReader in, ElectrumRPCClient electrumRPCClient) throws Throwable {
        sendRPCRequest(outputStream, electrumRPCClient);
        sendNewLine(outputStream);
        return getResponse(in, electrumRPCClient);
    }

    abstract void sendRPCRequest(OutputStream outputStream, ElectrumRPCClient electrumRPCClient) throws IOException;

    abstract T getResponse(BufferedReader in, ElectrumRPCClient electrumRPCClient) throws Throwable;

    /**
     * Get the result of the connection request.
     * @return Returns the result of the connection request.
     * @throws InterruptedException When the thread is interrupted.
     */
    public synchronized T getResult() throws Throwable {
        while (threadResult == null)
            wait();

        if (threadResult.getException() != null) {
            throw threadResult.getException();
        }

        return threadResult.getResult();
    }

    private void waitForResultClose(T result) {
        result.waitUntilComplete();
    }

    public class ThreadResult {
        private final T result;
        private final Throwable exception;

        public ThreadResult(T result, Throwable exception) {
            this.result = result;
            this.exception = exception;
        }

        public T getResult() {
            return result;
        }

        public Throwable getException() {
            return exception;
        }
    }

}
