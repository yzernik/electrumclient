package io.github.yzernik.electrumclient;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.stream.Stream;

public class ElectrumClient {
    private OutputStream clientOutputStream;
    private PrintWriter out;
    private BufferedReader in;

    private final InetAddress address;
    private final int port;

    public ElectrumClient(String host, int port) throws UnknownHostException {
        this.address = AddressLookup.getInetAddress(host);
        this.port = port;
    }

    public void start() throws IOException {
        Socket clientSocket = new Socket(address, port);
        clientOutputStream = clientSocket.getOutputStream();
        out = new PrintWriter(clientOutputStream, true);
        InputStream socketInputStream = clientSocket.getInputStream();
        in = new BufferedReader(new InputStreamReader(socketInputStream));
    }

    public void sendMessage(String msg) {
        out.println(msg);
    }

    public void sendNewLine() {
        out.println();
    }

    public void sendSubscribeMessage() {
        String subscribeMessage = "{ \"id\": \"blk\", \"method\": \"blockchain.headers.subscribe\"}";
        System.out.println(subscribeMessage);
        sendMessage(subscribeMessage);
    }

    public void sendGetBlockHeaderMessage() {
        String getBlockHeaderMessage = "{\"id\":\"1538775738\",\"jsonrpc\":\"2.0\",\"method\":\"blockchain.block.header\",\"params\":[23]}";
        System.out.println(getBlockHeaderMessage);
        sendMessage(getBlockHeaderMessage);
    }

    public void sendGetBlockHeaderMessageWithRPCClient() throws IOException {
        //String getBlockHeaderMessage = "{ \"id\": \"blk\", \"method\": \"blockchain.block.header\", \"params\": [23]}";
        //Log.i(getClass().getName(), getBlockHeaderMessage);
        //sendMessage(getBlockHeaderMessage);

        ElectrumRPCClient electrumRPCClient = new ElectrumRPCClient(clientOutputStream);
        electrumRPCClient.makeRequestGetBlockHeader(23);
        sendNewLine();
    }

    public void sendSubscribeMessageWithRPCClient() throws IOException {
        // String subscribeMessage = "{ \"id\": \"blk\", \"method\": \"blockchain.headers.subscribe\"}";
        // Log.i(getClass().getName(), subscribeMessage);
        // sendMessage(subscribeMessage);

        ElectrumRPCClient electrumRPCClient = new ElectrumRPCClient(clientOutputStream);
        electrumRPCClient.makeRequestSubscribeBlockHeaders();
        sendNewLine();
    }

    public Stream<String> getResponseLines() {
        return in.lines();
    }

    public String getResponseLine() throws IOException {
        return in.readLine();
    }

}
