package io.github.yzernik.electrumclient;

import java.io.*;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

abstract class ElectrumClientConnection<T extends ElectrumClientResponse> {

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

    public T makeRequest() throws IOException {
        try(
                Socket clientSocket = new Socket(address, port);
                OutputStream clientOutputStream = clientSocket.getOutputStream();
                PrintWriter out = new PrintWriter(clientOutputStream, true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ) {
            return makeRequest(clientOutputStream, out, in);
        } catch (IOException e) {
            throw e;
        }
    }

    private void sendNewLine(PrintWriter out) throws IOException {
        out.println();
    }

    public T makeRequest(OutputStream outputStream, PrintWriter out, BufferedReader in) throws IOException {
        sendRPCRequest(outputStream);
        sendNewLine(out);
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
