package io.github.yzernik.electrumclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

abstract class ElectrumClientConnection<T extends ElectrumClientResponse> {

    private String host;
    private int port;

    public ElectrumClientConnection(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public T makeRequest() throws IOException {
        InetAddress address = ElectrumClient.getInetAddress(host);
        try(
                Socket clientSocket = new Socket(address, port);
                OutputStream clientOutputStream = clientSocket.getOutputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ) {
            return makeRequest(clientOutputStream, in);
        } catch (IOException e) {
            throw e;
        }
    }

    private void sendNewLine(OutputStream outputStream) throws IOException {
        // out.println();
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

}
