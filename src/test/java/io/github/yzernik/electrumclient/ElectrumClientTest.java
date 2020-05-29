package io.github.yzernik.electrumclient;

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
        SubscribeHeadersClientConnection connection = electrumClient.subscribeHeaders();
        SubscribeHeadersResponse response = connection.getResult();
        connection.close();

        System.out.println("Got header notification: " + response);
        assertEquals(HEADER_LENGTH_HEX, response.hex.length());
        assert response.height >= 631359;
    }

    @Test(expected = IOException.class)
    public void getBlockHeaderWithConnectionClassBadServer() throws Exception {
        GetHeaderClientConnection clientConnection = new GetHeaderClientConnection("foooooooo", ELECTRUM_PORT, 0);

        Thread t =new Thread(clientConnection);
        t.start();

        clientConnection.getResult();
    }

}