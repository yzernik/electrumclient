package io.github.yzernik.electrumclient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.util.stream.Stream;

public class ElectrumClient {
    private OutputStream clientOutputStream;
    private InputStream clientInputStream;
    private PrintWriter out;
    private BufferedReader in;

    private final String host;
    private final int port;

    public ElectrumClient(String host, int port) throws UnknownHostException {
        this.host = host;
        this.port = port;
    }

    public String getHeader(int height) throws Throwable {
        GetHeaderClientConnection connection = new GetHeaderClientConnection(host, port, height);

        Thread t =new Thread(connection);
        t.start();

        ElectrumClientSingleLineResponse<GetHeaderResponse> response = connection.getResult();
        GetHeaderResponse getHeaderResponse = response.getLine();
        return getHeaderResponse.hex;
    }

    public Stream<SubscribeHeadersResponse> subscribeHeaders() throws Throwable {
        SubscribeHeadersClientConnection connection = new SubscribeHeadersClientConnection(host, port);

        Thread t =new Thread(connection);
        t.start();

        ElectrumClientMultiLineResponse<SubscribeHeadersResponse> response = connection.getResult();
        SubscribeHeadersResponse subscribeHeadersResponse = response.getLine();
        Stream<SubscribeHeadersResponse> notifications = response.getLines();
        return Stream.concat(Stream.of(subscribeHeadersResponse), notifications);
    }

}
