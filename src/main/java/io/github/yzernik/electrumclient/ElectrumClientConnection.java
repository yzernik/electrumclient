package io.github.yzernik.electrumclient;

import io.github.yzernik.electrumclient.exceptions.ElectrumRPCParseException;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.Callable;

abstract class ElectrumClientConnection<T extends ElectrumResponse> implements Callable<T> {

    private final static int DEFAULT_SOCKET_TIMEOUT = 0;

    private final InetSocketAddress address;
    private int socketTimeout;

    public ElectrumClientConnection(InetSocketAddress address) {
        this(address, DEFAULT_SOCKET_TIMEOUT);
    }

    public ElectrumClientConnection(InetSocketAddress address, int socketTimeout) {
        this.address = address;
        this.socketTimeout = socketTimeout;
    }

    @Override
    public T call() throws IOException, ElectrumRPCParseException {
        return makeRequest();
    }

    public T makeRequest() throws IOException, ElectrumRPCParseException {
        try(
                Socket clientSocket = new Socket(address.getAddress(), address.getPort());
                OutputStream clientOutputStream = clientSocket.getOutputStream();
                InputStream clientInputStream = clientSocket.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(clientInputStream))
        ) {
            clientSocket.setSoTimeout(socketTimeout);
            ElectrumRPCClient electrumRPCClient = new ElectrumRPCClient();
            sendRequest(clientOutputStream, electrumRPCClient);
            T result = getResponse(in, electrumRPCClient);
            // requestResult = new RequestResult(result, null);
            return result;
        }
    }

    public void sendRequest(OutputStream outputStream, ElectrumRPCClient electrumRPCClient) throws IOException, ElectrumRPCParseException {
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
        if (responseLine == null) {
            throw new ElectrumRPCParseException("Null response line.");
        }

        T responseItem = parseResponseLine(responseLine, electrumRPCClient);
        System.out.println("Got responseItem: " + responseItem);

        return responseItem;
    }

    abstract T parseResponseLine(String line, ElectrumRPCClient electrumRPCClient) throws ElectrumRPCParseException;

}
