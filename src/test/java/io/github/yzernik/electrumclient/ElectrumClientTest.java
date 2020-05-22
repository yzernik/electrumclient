package io.github.yzernik.electrumclient;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.Stream;


public class ElectrumClientTest {

    private static final String ELECTRUM_HOST = "currentlane.lovebitco.in";
    private static final int ELECTRUM_PORT = 50001;

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
    public void getBlockHeaderWithConnectionClass() throws Exception {
        GetHeaderClientConnection clientConnection = new GetHeaderClientConnection(ELECTRUM_HOST, ELECTRUM_PORT);

        clientConnection.makeRequest();
        ElectrumClientSingleLineResponse response = clientConnection.getResult();
        String blockString = response.getLine();

        System.out.println(blockString);
        assert blockString.startsWith("{\"jsonrpc\": \"2.0\", \"result\":");
    }


    @Test
    public void subscribeBlockHeadersWithConnectionClass() throws Exception {
        SubscribeHeadersClientConnection clientConnection = new SubscribeHeadersClientConnection(ELECTRUM_HOST, ELECTRUM_PORT);

        clientConnection.makeRequest();
        ElectrumClientMultiLineResponse response = clientConnection.getResult();
        Stream<String> blockStringStream = response.getLines();
        String currentBlockString = blockStringStream.findFirst().get();

        System.out.println(currentBlockString);
        assert currentBlockString.startsWith("{\"jsonrpc\": \"2.0\", \"result\":");
    }

}