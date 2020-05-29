package io.github.yzernik.electrumclient.examples;

import io.github.yzernik.electrumclient.ElectrumClient;
import io.github.yzernik.electrumclient.SubscribeHeadersClientConnection;
import io.github.yzernik.electrumclient.SubscribeHeadersResponse;

public class SubscribeBlockHeadersExample {

    // Public electrum server from https://1209k.com/bitcoin-eye/ele.php?chain=btc
    private static final String ELECTRUM_HOST = "electrumx-core.1209k.com";
    private static final int ELECTRUM_PORT = 50001;

    public static void main(String[] args) throws Exception {
        ElectrumClient electrumClient = new ElectrumClient(ELECTRUM_HOST, ELECTRUM_PORT);
        SubscribeHeadersClientConnection connection = electrumClient.subscribeHeaders();

        SubscribeHeadersResponse response = connection.getResult();
        System.out.println("Got initial response");
        System.out.println(response);

        Thread.sleep(10000);
        System.out.println("Closing connection");
        connection.close();
    }

}
