package io.github.yzernik.electrumclient;

import io.github.yzernik.electrumclient.subscribepeers.SubscribePeersResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.util.concurrent.*;

import static org.junit.Assert.assertEquals;


public class ElectrumClientTest {

    private static final String ELECTRUM_HOST = "electrumx-core.1209k.com";
    private static final int ELECTRUM_PORT = 50001;
    private static final int HEADER_LENGTH_HEX = 160;

    private ExecutorService executorService;

    @Before
    public void setup() {
        executorService = Executors.newSingleThreadExecutor();
    }

    @After
    public void teardown() {
        System.out.println("Shutting down executorservice...");
        executorService.shutdown();
    }

    @Test
    public void getBlockHeader() throws Exception {
        InetSocketAddress address = new InetSocketAddress(ELECTRUM_HOST, ELECTRUM_PORT);
        ElectrumClient electrumClient = new ElectrumClient(address, executorService);
        Future<GetHeaderResponse> responseFuture = electrumClient.getHeader(631515);

        GetHeaderResponse response = responseFuture.get();
        System.out.println("block header hex: " + response.hex);
        System.out.println("is task done: " + responseFuture.isDone());

        assertEquals(HEADER_LENGTH_HEX, response.hex.length());
    }

    /**
     * Take two items from the notification handler, and then stop the connection.
     * @throws Exception
     */
    @Test
    public void subscribeBlockHeaders() throws Exception {
        BlockingQueue<SubscribeHeadersResponse> responsesQueue = new LinkedBlockingQueue<>();

        InetSocketAddress address = new InetSocketAddress(ELECTRUM_HOST, ELECTRUM_PORT);
        ElectrumClient electrumClient = new ElectrumClient(address, executorService);
        NotificationHandler<SubscribeHeadersResponse> notificationHandler =
                notification -> {
            System.out.println("Handling notification in test: " + notification);
            responsesQueue.add(notification);
        };

        System.out.println("Calling subscribeHeaders.");
        Future<SubscribeHeadersResponse> responseFuture = electrumClient.subscribeHeaders(notificationHandler);
        System.out.println("Got response future.");

        SubscribeHeadersResponse firstResponse = responsesQueue.take();
        System.out.println("Got first response: " + firstResponse);
        assertEquals(HEADER_LENGTH_HEX, firstResponse.hex.length());
        assert firstResponse.height >= 631359;

        SubscribeHeadersResponse secondResponse = responsesQueue.take();
        System.out.println("Got second response: " + secondResponse);
        assertEquals(HEADER_LENGTH_HEX, secondResponse.hex.length());
        assert secondResponse.height >= 631359;

        responseFuture.cancel(true);
    }


    @Test
    public void testSubscribePeers() throws Exception {
        InetSocketAddress address = new InetSocketAddress(ELECTRUM_HOST, ELECTRUM_PORT);
        ElectrumClient electrumClient = new ElectrumClient(address, executorService);
        Future<SubscribePeersResponse> responseFuture = electrumClient.subscribePeers();

        SubscribePeersResponse response = responseFuture.get();
        System.out.println("Subscribe peers response: " + response);

        assert(response.peers.size() > 0);
    }

    @Test(expected = ExecutionException.class)
    public void getBlockHeaderBadServer() throws Exception {
        InetSocketAddress address = new InetSocketAddress("foooooooo", ELECTRUM_PORT);
        ElectrumClient electrumClient = new ElectrumClient(address, executorService);
        Future<GetHeaderResponse> responseFuture = electrumClient.getHeader(0);
        responseFuture.get();
    }

}