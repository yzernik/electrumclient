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
    public void getBlockHeader() throws Throwable {
        ElectrumClient electrumClient = new ElectrumClient(ELECTRUM_HOST, ELECTRUM_PORT);
        String hex = electrumClient.getHeader(0);

        System.out.println(hex);
        assertEquals(HEADER_LENGTH_HEX, hex.length());
    }

    @Test
    public void subscribeBlockHeaders() throws Throwable {
        ElectrumClient electrumClient = new ElectrumClient(ELECTRUM_HOST, ELECTRUM_PORT);
        Stream<SubscribeHeadersResponse> responseStream = electrumClient.subscribeHeaders();

        AtomicInteger counter = new AtomicInteger(0);

        responseStream.limit(2).forEach(header -> {
            System.out.println(header);
            System.out.println(header.hex);
            System.out.println(header.height);
            assertEquals(HEADER_LENGTH_HEX, header.hex.length());
            assert header.height >= 631359;
            counter.incrementAndGet();
        });

        assertEquals(2, counter.get());
    }

    @Test
    public void getBlockHeaderWithConnectionClass() throws Throwable {
        GetHeaderClientConnection clientConnection = new GetHeaderClientConnection(ELECTRUM_HOST, ELECTRUM_PORT, 0);

        Thread t =new Thread(clientConnection);
        t.start();

        ElectrumClientSingleLineResponse<GetHeaderResponse> response = clientConnection.getResult();
        GetHeaderResponse getHeaderResponse = response.getLine();

        System.out.println(getHeaderResponse.hex);
        assertEquals(HEADER_LENGTH_HEX, getHeaderResponse.hex.length());
    }


    @Test
    public void subscribeBlockHeadersWithConnectionClass() throws Throwable {
        SubscribeHeadersClientConnection clientConnection = new SubscribeHeadersClientConnection(ELECTRUM_HOST, ELECTRUM_PORT);

        Thread t =new Thread(clientConnection);
        t.start();

        ElectrumClientMultiLineResponse<SubscribeHeadersResponse> response = clientConnection.getResult();
        System.out.println("Got response from server.");

        // Test the initial response line
        SubscribeHeadersResponse responseHeader = response.getLine();
        System.out.println("responseHeader");
        System.out.println(responseHeader);
        System.out.println(responseHeader.hex);
        System.out.println(responseHeader.height);
        assertEquals(HEADER_LENGTH_HEX, responseHeader.hex.length());
        assert responseHeader.height >= 631359;

        // Test the streaming notification lines
        Stream<SubscribeHeadersResponse> responseStream = response.getLines();
        // SubscribeHeadersResponse firstResponse = responseStream.findFirst().get();
        // headerStream.close();

        SubscribeHeadersResponse firstStreamHeader = responseStream.findFirst().get();
        System.out.println(firstStreamHeader);
        System.out.println(firstStreamHeader.hex);
        System.out.println(firstStreamHeader.height);
        assertEquals(HEADER_LENGTH_HEX, firstStreamHeader.hex.length());
        assert firstStreamHeader.height >= 631359;
    }


    @Test(expected = IOException.class)
    public void getBlockHeaderWithConnectionClassBadServer() throws Throwable {
        GetHeaderClientConnection clientConnection = new GetHeaderClientConnection("foooooooo", ELECTRUM_PORT, 0);

        Thread t =new Thread(clientConnection);
        t.start();

        clientConnection.getResult();
    }

}