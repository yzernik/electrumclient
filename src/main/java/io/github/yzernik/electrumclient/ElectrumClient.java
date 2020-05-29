package io.github.yzernik.electrumclient;

public class ElectrumClient {

    private final String host;
    private final int port;

    public ElectrumClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Get the hex-encoded block header at the given height.
     * @param height The block height of the requested header.
     * @return A connection that returns a hex-encoded block header
     * @throws
     */
    public GetHeaderClientConnection getHeader(int height) throws Exception {
        GetHeaderClientConnection connection = new GetHeaderClientConnection(host, port, height);

        Thread t = new Thread(connection);
        t.start();

        return connection;
    }

    /**
     * Subscribe to new blocks, and get notifications as a stream of block headers.
     * @return Stream of block headers
     * @throws
     */
    public SubscribeHeadersClientConnection subscribeHeaders(NotificationHandler<SubscribeHeadersResponse> notificationHandler) throws Exception {
        SubscribeHeadersClientConnection connection = new SubscribeHeadersClientConnection(host, port, notificationHandler);

        Thread t = new Thread(connection);
        t.start();

        return connection;
    }

}
