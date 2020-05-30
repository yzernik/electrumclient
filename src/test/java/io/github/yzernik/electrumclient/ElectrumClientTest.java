package io.github.yzernik.electrumclient;

import io.github.yzernik.electrumclient.exceptions.ElectrumClientException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
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
        ElectrumClient electrumClient = new ElectrumClient(ELECTRUM_HOST, ELECTRUM_PORT, executorService);
        Future<GetHeaderResponse> responseFuture = electrumClient.getHeader(631515);

        GetHeaderResponse response = responseFuture.get();
        System.out.println("block header hex: " + response.hex);
        System.out.println("is task done: " + responseFuture.isDone());

        assertEquals(HEADER_LENGTH_HEX, response.hex.length());
    }


    @Test
    public void subscribeBlockHeaders() throws Exception {
        BlockingQueue<SubscribeHeadersResponse> responsesQueue = new LinkedBlockingQueue<>();

        ElectrumClient electrumClient = new ElectrumClient(ELECTRUM_HOST, ELECTRUM_PORT);
        NotificationHandler<SubscribeHeadersResponse> notificationHandler =
                notification -> {
            System.out.println(notification);
            responsesQueue.add(notification);
        };

        System.out.println("Calling subscribeHeaders.");
        Future<SubscribeHeadersResponse> responseFuture = electrumClient.subscribeHeaders(notificationHandler);
        System.out.println("Got response future.");
        Thread.sleep(10000);
        responseFuture.cancel(true);

        SubscribeHeadersResponse firstResponse = responsesQueue.take();
        System.out.println("Got header notification: " + firstResponse);
        assertEquals(HEADER_LENGTH_HEX, firstResponse.hex.length());
        assert firstResponse.height >= 631359;
    }

    /*
    @Test(expected = ElectrumClientException.class)
    public void getBlockHeaderBadServer() throws Exception {
        ElectrumClient electrumClient = new ElectrumClient("foooooooo", ELECTRUM_PORT);
        GetHeaderClientConnection connection = electrumClient.getHeader(0);
        GetHeaderResponse response = connection.getResult();

        assertEquals(HEADER_LENGTH_HEX, response.hex.length());
    }*/

}