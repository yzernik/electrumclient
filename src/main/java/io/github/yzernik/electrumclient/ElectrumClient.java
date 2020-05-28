package io.github.yzernik.electrumclient;

import java.util.stream.Stream;

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
     * @return A hex-encoded block header
     * @throws
     */
    public String getHeader(int height) throws Exception {
        GetHeaderClientConnection connection = new GetHeaderClientConnection(host, port, height);

        Thread t =new Thread(connection);
        t.start();

        ElectrumClientSingleLineResponse<GetHeaderMessage> response = connection.getResult();
        GetHeaderMessage getHeaderResponse = response.getLine();
        return getHeaderResponse.hex;
    }

    /**
     * Subscribe to new blocks, and get notifications as a stream of block headers.
     * @return Stream of block headers
     * @throws
     */
    public Stream<SubscribeHeadersMessage> subscribeHeaders() throws Exception {
        SubscribeHeadersClientConnection connection = new SubscribeHeadersClientConnection(host, port);

        Thread t =new Thread(connection);
        t.start();

        ElectrumClientSubscribeResponse<SubscribeHeadersMessage> response = connection.getResult();
        SubscribeHeadersMessage subscribeHeadersResponse = response.getLine();
        Stream<SubscribeHeadersMessage> notifications = response.getLines();
        return Stream.concat(Stream.of(subscribeHeadersResponse), notifications);
    }

}
