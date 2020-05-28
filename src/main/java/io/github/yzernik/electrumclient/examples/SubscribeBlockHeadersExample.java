package io.github.yzernik.electrumclient.examples;

import io.github.yzernik.electrumclient.ElectrumClient;
import io.github.yzernik.electrumclient.SubscribeHeadersMessage;

import java.util.stream.Stream;

public class SubscribeBlockHeadersExample {

    // Public electrum server from https://1209k.com/bitcoin-eye/ele.php?chain=btc
    private static final String ELECTRUM_HOST = "electrumx-core.1209k.com";
    private static final int ELECTRUM_PORT = 50001;

    public static void main(String[] args) throws Exception {
        ElectrumClient electrumClient = new ElectrumClient(ELECTRUM_HOST, ELECTRUM_PORT);
        Stream<SubscribeHeadersMessage> headers = electrumClient.subscribeHeaders();

        System.out.println("Starting to read from stream");
        headers.forEach(header -> {
            System.out.println(header);
        });
        System.out.println("Finished reading from stream");
    }

}
