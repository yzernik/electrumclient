package io.github.yzernik.electrumclient;

import io.github.yzernik.electrumclient.exceptions.ElectrumClientException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;


public class ElectrumClientTest {

    private static final String ELECTRUM_HOST = "electrumx-core.1209k.com";
    private static final int ELECTRUM_PORT = 50001;
    private static final int HEADER_LENGTH_HEX = 160;

    @Before
    public void setup() {
        // Nothing
    }

    @After
    public void teardown() {
        // Nothing
    }

    @Test
    public void getBlockHeader() throws Exception {
        ElectrumClient electrumClient = new ElectrumClient(ELECTRUM_HOST, ELECTRUM_PORT);
        GetHeaderClientConnection connection = electrumClient.getHeader(0);
        GetHeaderResponse response = connection.getResult();

        assertEquals(HEADER_LENGTH_HEX, response.hex.length());
    }

    @Test
    public void subscribeBlockHeaders() throws Exception {
        ElectrumClient electrumClient = new ElectrumClient(ELECTRUM_HOST, ELECTRUM_PORT);
        NotificationHandler<SubscribeHeadersResponse> notificationHandler =
                notification -> System.out.println(notification);

        SubscribeHeadersClientConnection connection = electrumClient.subscribeHeaders(notificationHandler);
        SubscribeHeadersResponse response = connection.getResult();
        connection.close();

        System.out.println("Got header notification: " + response);
        assertEquals(HEADER_LENGTH_HEX, response.hex.length());
        assert response.height >= 631359;
    }

    @Test(expected = ElectrumClientException.class)
    public void getBlockHeaderBadServer() throws Exception {
        ElectrumClient electrumClient = new ElectrumClient("foooooooo", ELECTRUM_PORT);
        GetHeaderClientConnection connection = electrumClient.getHeader(0);
        GetHeaderResponse response = connection.getResult();

        assertEquals(HEADER_LENGTH_HEX, response.hex.length());
    }

}