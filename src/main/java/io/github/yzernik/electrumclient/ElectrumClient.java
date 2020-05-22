package io.github.yzernik.electrumclient;

import java.io.*;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.stream.Stream;

public class ElectrumClient {
    private Socket clientSocket;
    private OutputStream clientOutputStream;
    private PrintWriter out;
    private BufferedReader in;

    private InetAddress address;
    private int port;

    public ElectrumClient(String host, int port) throws UnknownHostException {
        this.address = ElectrumClient.getInetAddress(host);
        this.port = port;
    }

    public ElectrumClient(InetAddress address, int port) {
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

    public void sendMessage(String msg) throws IOException {
        out.println(msg);
        //String resp = in.readLine();
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

    public void sendSubscribeMessage() throws IOException {
        String subscribeMessage = "{ \"id\": \"blk\", \"method\": \"blockchain.headers.subscribe\"}";
        System.out.println(subscribeMessage);
        sendMessage(subscribeMessage);
    }

    public void sendGetBlockHeaderMessage() throws IOException {
        String getBlockHeaderMessage = "{ \"id\": \"1538775738\", \"jsonrpc\":\"2.0\", \"method\": \"blockchain.block.header\", \"params\": [23]}";
        System.out.println(getBlockHeaderMessage);
        sendMessage(getBlockHeaderMessage);
    }

    public void sendGetBlockHeaderMessageWithRPCClient() throws IOException {
        //String getBlockHeaderMessage = "{ \"id\": \"blk\", \"method\": \"blockchain.block.header\", \"params\": [23]}";
        //Log.i(getClass().getName(), getBlockHeaderMessage);
        //sendMessage(getBlockHeaderMessage);

        ElectrumRPCClient electrumRPCClient = new ElectrumRPCClient(clientOutputStream);
        electrumRPCClient.makeRequestGetBlockHeader(23);
    }


    public void sendSubscribeMessageWithRPCClient() throws IOException {
        // String subscribeMessage = "{ \"id\": \"blk\", \"method\": \"blockchain.headers.subscribe\"}";
        // Log.i(getClass().getName(), subscribeMessage);
        // sendMessage(subscribeMessage);

        ElectrumRPCClient electrumRPCClient = new ElectrumRPCClient(clientOutputStream);
        electrumRPCClient.makeRequestSubscribeBlockHeaders();
    }

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

    public Stream<String> getResponseLines() throws IOException {
        // sendSubscribeMessage();

        //read file into stream, try-with-resources
        return in.lines();
    }

    public String getResponseLine() throws IOException {
        // sendGetBlockHeaderMessage();

        return in.readLine();
    }

}
