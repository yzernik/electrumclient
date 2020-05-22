package io.github.yzernik.electrumclient;

import java.io.*;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

abstract class ElectrumClientConnection<T extends ElectrumClientResponse> {

    private Socket clientSocket;
    private OutputStream clientOutputStream;
    private PrintWriter out;
    private BufferedReader in;

    private InetAddress address;
    private int port;

    public ElectrumClientConnection(String host, int port) throws UnknownHostException {
        this.address = ElectrumClient.getInetAddress(host);
        this.port = port;
    }

    public ElectrumClientConnection(InetAddress address, int port) {
        this.address = address;
        this.port = port;
    }

    public void start() throws IOException {
        clientSocket = new Socket(address, port);
        clientOutputStream = clientSocket.getOutputStream();
        out = new PrintWriter(clientOutputStream, true);
        InputStream socketInputStream = clientSocket.getInputStream();
        in = new BufferedReader(new InputStreamReader(socketInputStream));
    }

    private void sendNewLine() throws IOException {
        out.println();
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

    public T makeRequest() throws IOException {
        start();
        sendRPCRequest();
        sendNewLine();
        return getResponse();
    }

    public void sendRequestWithRPCClient() throws IOException {
        sendRPCRequest();
        sendNewLine();
    }

    abstract void sendRPCRequest() throws IOException;

    abstract T getResponse() throws IOException;

    public static InetAddress selectAddress(InetAddress[] addresses) {
        // Get ip6 address if available
        for (InetAddress addr : addresses) {
            if (addr instanceof Inet6Address) {
                return (Inet6Address) addr;
            }
        }

        // Get any address if available
        if(addresses.length > 0) {
            return addresses[0];
        }

        return null;
    }

    public static InetAddress getInetAddress(String host) throws UnknownHostException {
        InetAddress[] addresses = InetAddress.getAllByName(host);
        return selectAddress(addresses);
    }

    public OutputStream getClientOutputStream() {
        return clientOutputStream;
    }

    public BufferedReader getIn() {
        return in;
    }

}
