package io.github.yzernik.electrumclient;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

abstract class ElectrumClientConnection<T extends ElectrumClientResponse> implements Runnable {

    private final static int DEFAULT_SOCKET_TIMEOUT = 0;

    private final String host;
    private final int port;
    private ThreadResult threadResult = null;
    private int socketTimeout;

    public ElectrumClientConnection(String host, int port) {
        this.host = host;
        this.port = port;
        this.socketTimeout = DEFAULT_SOCKET_TIMEOUT;
    }

    public ElectrumClientConnection(String host, int port, int socketTimeout) {
        this.host = host;
        this.port = port;
        this.socketTimeout = socketTimeout;
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
                clientSocket.setSoTimeout(socketTimeout);
                ElectrumRPCClient electrumRPCClient = new ElectrumRPCClient();
                T result = makeRequest(clientOutputStream, in, electrumRPCClient);
                threadResult = new ThreadResult(result, null);

                // Wake up threads blocked on the getResult() method
                synchronized(this) {
                    notifyAll();
                }
                System.out.println("Notified on available result");

                System.out.println("Waiting for close...");
                waitForResultClose(result);
                System.out.println("Closed result");
            }
        } catch (Throwable e) {
            e.printStackTrace(System.out);
            threadResult = new ThreadResult(null, e);
        } finally {
            System.out.println("Finished connection thread.");
            synchronized(this) {
                notifyAll();
            }
        }

    }

    public T makeRequest(OutputStream outputStream, BufferedReader in, ElectrumRPCClient electrumRPCClient) throws Throwable {
        sendRPCRequest(outputStream, electrumRPCClient);
        return getResponse(in, electrumRPCClient);
    }

    abstract String getRPCRequest(ElectrumRPCClient electrumRPCClient) throws IOException;

    private void sendRPCRequest(OutputStream outputStream, ElectrumRPCClient electrumRPCClient) throws IOException {
        String requestString = getRPCRequest(electrumRPCClient);
        outputStream.write(requestString.getBytes());
        outputStream.write('\n');
        outputStream.flush();
    }

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
