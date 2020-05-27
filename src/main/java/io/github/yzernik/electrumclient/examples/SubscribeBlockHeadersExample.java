package io.github.yzernik.electrumclient.examples;

import io.github.yzernik.electrumclient.ElectrumClient;
import io.github.yzernik.electrumclient.SubscribeHeadersResponse;

import java.util.stream.Stream;

public class SubscribeBlockHeadersExample {

    private static final String ELECTRUM_HOST = "electrum-server.ninja";
    private static final int ELECTRUM_PORT = 50001;

    public static void main(String[] args) throws Throwable {
        ElectrumClient electrumClient = new ElectrumClient(ELECTRUM_HOST, ELECTRUM_PORT);
        Stream<SubscribeHeadersResponse> headers = electrumClient.subscribeHeaders();

        System.out.println("Starting to read from stream");
        headers.forEach(header -> {
            System.out.println(header);
        });
        System.out.println("Finished reading from stream");
    }

}
