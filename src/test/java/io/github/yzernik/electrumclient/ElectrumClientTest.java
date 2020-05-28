package io.github.yzernik.electrumclient;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;


public class ElectrumClientTest {

    private static final String ELECTRUM_HOST = "electrum-server.ninja";
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
        String hex = electrumClient.getHeader(0);

        assertEquals(HEADER_LENGTH_HEX, hex.length());
    }

    @Test
    public void subscribeBlockHeaders() throws Exception {
        ElectrumClient electrumClient = new ElectrumClient(ELECTRUM_HOST, ELECTRUM_PORT);
        Stream<SubscribeHeadersResponse> responseStream = electrumClient.subscribeHeaders();

        AtomicInteger counter = new AtomicInteger(0);

        responseStream.limit(2).forEach(header -> {
            System.out.println("Got header notification: " + header);
            assertEquals(HEADER_LENGTH_HEX, header.hex.length());
            assert header.height >= 631359;
            counter.incrementAndGet();
        });

        assertEquals(2, counter.get());
    }

    @Test
    public void getBlockHeaderWithConnectionClass() throws Exception {
        GetHeaderClientConnection clientConnection = new GetHeaderClientConnection(ELECTRUM_HOST, ELECTRUM_PORT, 0);

        Thread t =new Thread(clientConnection);
        t.start();

        ElectrumClientSingleLineResult<GetHeaderResponse> response = clientConnection.getResult();
        GetHeaderResponse getHeaderResponse = response.getLine();

        assertEquals(HEADER_LENGTH_HEX, getHeaderResponse.hex.length());
    }


    @Test
    public void subscribeBlockHeadersWithConnectionClass() throws Exception {
        SubscribeHeadersClientConnection clientConnection = new SubscribeHeadersClientConnection(ELECTRUM_HOST, ELECTRUM_PORT);

        Thread t =new Thread(clientConnection);
        t.start();

        ElectrumClientSubscribeResult<SubscribeHeadersResponse> response = clientConnection.getResult();

        // Test the initial response line
        SubscribeHeadersResponse responseHeader = response.getLine();
        assertEquals(HEADER_LENGTH_HEX, responseHeader.hex.length());
        assert responseHeader.height >= 631359;

        // Test the streaming notification lines
        Stream<SubscribeHeadersResponse> responseStream = response.getLines();
        // SubscribeHeadersResponse firstResponse = responseStream.findFirst().get();
        // headerStream.close();

        SubscribeHeadersResponse firstStreamHeader = responseStream.findFirst().get();
        assertEquals(HEADER_LENGTH_HEX, firstStreamHeader.hex.length());
        assert firstStreamHeader.height >= 631359;
    }


    @Test(expected = IOException.class)
    public void getBlockHeaderWithConnectionClassBadServer() throws Exception {
        GetHeaderClientConnection clientConnection = new GetHeaderClientConnection("foooooooo", ELECTRUM_PORT, 0);

        Thread t =new Thread(clientConnection);
        t.start();

        clientConnection.getResult();
    }

}