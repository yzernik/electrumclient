package io.github.yzernik.electrumclient;

import io.github.yzernik.electrumclient.subscribepeers.SubscribePeersResponse;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ElectrumClient {

    private final String host;
    private final int port;
    private ExecutorService executorService;

    public ElectrumClient(String host, int port, ExecutorService executorService) {
        this.host = host;
        this.port = port;
        this.executorService = executorService;
    }

    public ElectrumClient(String host, int port) {
        this(host, port, Executors.newSingleThreadExecutor());
    }

    /**
     * Get the hex-encoded block header at the given height.
     * @param height The block height of the requested header.
     * @return Future of a getheader response object.
     */
    public Future<GetHeaderResponse> getHeader(int height) {
        GetHeaderClientConnection task = new GetHeaderClientConnection(host, port, height);
        return executorService.submit(task);
    }

    /**
     * Subscribe to new blocks, and get notifications as a stream of block headers.
     * @return Future of a subscribeheadersresponse response object.
     */
    public Future<SubscribeHeadersResponse> subscribeHeaders(NotificationHandler<SubscribeHeadersResponse> notificationHandler) {
        SubscribeHeadersClientConnection task = new SubscribeHeadersClientConnection(host, port, notificationHandler);
        return executorService.submit(task);
    }


    /**
     * Get the connected peers. This is not actually a subscription.
     * @return Future of a subscribepeersresponse object.
     */
    public Future<SubscribePeersResponse> subscribePeers() {
        SubscribePeersClientConnection task = new SubscribePeersClientConnection(host, port);
        return executorService.submit(task);
    }

}
