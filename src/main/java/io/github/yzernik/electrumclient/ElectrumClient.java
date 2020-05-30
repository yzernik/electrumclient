package io.github.yzernik.electrumclient;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

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
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        return executorService.submit(task);
    }

    /**
     * Subscribe to new blocks, and get notifications as a stream of block headers.
     * @return Future of a subscribeheadersresponse response object.
     */
    public Future<SubscribeHeadersResponse> subscribeHeaders(NotificationHandler<SubscribeHeadersResponse> notificationHandler) {
        SubscribeHeadersClientConnection task = new SubscribeHeadersClientConnection(host, port, notificationHandler);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        return executorService.submit(task);
    }

}
