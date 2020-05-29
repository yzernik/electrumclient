package io.github.yzernik.electrumclient;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

abstract class ElectrumClientConnection<T extends ElectrumResponse> implements Runnable {

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
            // InetAddress address = AddressLookup.getInetAddress(host);
            InetAddress address = InetAddress.getByName(host);
            try(
                    Socket clientSocket = new Socket(address, port);
                    OutputStream clientOutputStream = clientSocket.getOutputStream();
                    InputStream clientInputStream = clientSocket.getInputStream();
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientInputStream))
            ) {
                clientSocket.setSoTimeout(socketTimeout);
                ElectrumRPCClient electrumRPCClient = new ElectrumRPCClient();
                makeRequest(clientOutputStream, in, electrumRPCClient);
                T result = getResponse(in, electrumRPCClient);
                threadResult = new ThreadResult(result, null);

                // Wake up threads blocked on the getResult() method
                synchronized(this) {
                    notifyAll();
                }

                handleNotifications(in, electrumRPCClient);
            }
        } catch (IOException e) {
            e.printStackTrace();
            threadResult = new ThreadResult(null, e);
        } catch (ElectrumRPCParseException e) {
            e.printStackTrace();
            threadResult = new ThreadResult(null, e);
        } finally {
            synchronized(this) {
                notifyAll();
            }
        }

    }

    public void makeRequest(OutputStream outputStream, BufferedReader in, ElectrumRPCClient electrumRPCClient) throws IOException, ElectrumRPCParseException {
        sendRPCRequest(outputStream, electrumRPCClient);
    }

    abstract String getRPCRequest(ElectrumRPCClient electrumRPCClient) throws IOException;

    private void sendRPCRequest(OutputStream outputStream, ElectrumRPCClient electrumRPCClient) throws IOException {
        String requestString = getRPCRequest(electrumRPCClient);
        outputStream.write(requestString.getBytes());
        outputStream.write('\n');
        outputStream.flush();
    }

    T getResponse(BufferedReader in, ElectrumRPCClient electrumRPCClient) throws IOException, ElectrumRPCParseException {
        String responseLine = in.readLine();
        System.out.println("Got responseLine: " + responseLine);

        T responseItem = parseResponseLine(responseLine, electrumRPCClient);
        System.out.println("Got responseItem: " + responseItem);

        return responseItem;
    }

    void handleNotifications(BufferedReader in, ElectrumRPCClient electrumRPCClient) throws IOException, ElectrumRPCParseException {
        String notificationLine = in.readLine();
        while (notificationLine != null) {
            System.out.println("Handling notification line: " + notificationLine);
            T notification = parseNotification(notificationLine, electrumRPCClient);
            System.out.println("Handling notification: " + notification);
            // TODO: handle the notification
        }
    }

    abstract T parseResponseLine(String line, ElectrumRPCClient electrumRPCClient) throws ElectrumRPCParseException;

    abstract T parseNotification(String line, ElectrumRPCClient electrumRPCClient) throws ElectrumRPCParseException;


    /**
     * Get the result of the connection request.
     * @return Returns the result of the connection request.
     * @throws InterruptedException When the thread is interrupted.
     */
    public synchronized T getResult() throws Exception {
        while (threadResult == null)
            wait();

        if (threadResult.getException() != null) {
            throw threadResult.getException();
        }

        return threadResult.getResult();
    }

    public class ThreadResult {
        private final T result;
        private final Exception exception;

        public ThreadResult(T result, Exception exception) {
            this.result = result;
            this.exception = exception;
        }

        public T getResult() {
            return result;
        }

        public Exception getException() {
            return exception;
        }
    }

}
