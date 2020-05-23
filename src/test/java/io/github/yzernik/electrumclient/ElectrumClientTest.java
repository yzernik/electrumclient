package io.github.yzernik.electrumclient;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
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
        electrumClient.start();

        electrumClient.sendGetBlockHeaderMessage();
        String blockString = electrumClient.getResponseLine();

        System.out.println(blockString);
        assert blockString.startsWith("{\"jsonrpc\": \"2.0\", \"result\":");
    }

    @Test
    public void getBlockHeaderWithRPCClient() throws Exception {
        ElectrumClient electrumClient = new ElectrumClient(ELECTRUM_HOST, ELECTRUM_PORT);
        electrumClient.start();

        electrumClient.sendGetBlockHeaderMessageWithRPCClient();
        String blockString = electrumClient.getResponseLine();

        System.out.println(blockString);
        assert blockString.startsWith("{\"jsonrpc\": \"2.0\", \"result\":");
    }

    @Test
    public void subscribeBlockHeaders() throws Exception {
        ElectrumClient electrumClient = new ElectrumClient(ELECTRUM_HOST, ELECTRUM_PORT);
        electrumClient.start();

        electrumClient.sendSubscribeMessage();
        Stream<String> blockStringStream = electrumClient.getResponseLines();
        String currentBlockString = blockStringStream.findFirst().get();

        assert currentBlockString.startsWith("{\"jsonrpc\": \"2.0\", \"result\":");
    }

    @Test
    public void subscribeBlockHeadersWithRPCClient() throws Exception {
        ElectrumClient electrumClient = new ElectrumClient(ELECTRUM_HOST, ELECTRUM_PORT);
        electrumClient.start();

        electrumClient.sendSubscribeMessageWithRPCClient();
        Stream<String> blockStringStream = electrumClient.getResponseLines();
        String currentBlockString = blockStringStream.findFirst().get();

        assert currentBlockString.startsWith("{\"jsonrpc\": \"2.0\", \"result\":");
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

        ElectrumClientSingleLineResponse<SubscribeHeadersResponse> response = clientConnection.getResult();
        SubscribeHeadersResponse headerStream = response.getLine();
        // headerStream.close();

        System.out.println(headerStream);
        System.out.println(headerStream.hex);
        System.out.println(headerStream.height);
        assertEquals(HEADER_LENGTH_HEX, headerStream.hex.length());
        assert headerStream.height >= 631359;
    }


    @Test(expected = IOException.class)
    public void getBlockHeaderWithConnectionClassBadServer() throws Throwable {
        GetHeaderClientConnection clientConnection = new GetHeaderClientConnection("foooooooo", ELECTRUM_PORT, 0);

        Thread t =new Thread(clientConnection);
        t.start();

        clientConnection.getResult();
    }

}