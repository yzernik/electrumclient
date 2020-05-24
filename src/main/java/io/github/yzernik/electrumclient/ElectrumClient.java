package io.github.yzernik.electrumclient;

import java.net.UnknownHostException;
import java.util.stream.Stream;

public class ElectrumClient {

    private final String host;
    private final int port;

    public ElectrumClient(String host, int port) throws UnknownHostException {
        this.host = host;
        this.port = port;
    }

    /**
     * Get the hex-encoded block header at the given height.
     * @param height The block height of the requested header.
     * @return A hex-encoded block header
     * @throws Throwable
     */
    public String getHeader(int height) throws Throwable {
        GetHeaderClientConnection connection = new GetHeaderClientConnection(host, port, height);

        Thread t =new Thread(connection);
        t.start();

        ElectrumClientSingleLineResponse<GetHeaderResponse> response = connection.getResult();
        GetHeaderResponse getHeaderResponse = response.getLine();
        return getHeaderResponse.hex;
    }

    /**
     * Subscribe to new blocks, and get notifications as a stream of block headers.
     * @return Stream of block headers
     * @throws Throwable
     */
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
