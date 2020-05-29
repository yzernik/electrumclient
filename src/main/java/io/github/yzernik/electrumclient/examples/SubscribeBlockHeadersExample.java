package io.github.yzernik.electrumclient.examples;

import io.github.yzernik.electrumclient.*;
import io.github.yzernik.electrumclient.exceptions.ElectrumClientException;

import java.io.IOException;

public class SubscribeBlockHeadersExample {

    // Public electrum server from https://1209k.com/bitcoin-eye/ele.php?chain=btc
    private static final String ELECTRUM_HOST = "electrumx-core.1209k.com";
    private static final int ELECTRUM_PORT = 50001;

    public static void main(String[] args) throws InterruptedException, ElectrumClientException, IOException {
        ElectrumClient electrumClient = new ElectrumClient(ELECTRUM_HOST, ELECTRUM_PORT);
        NotificationHandler<SubscribeHeadersResponse> notificationHandler =
                notification -> System.out.println(notification);

        SubscribeHeadersClientConnection connection = electrumClient.subscribeHeaders(notificationHandler);

        SubscribeHeadersResponse response = connection.getResult();
        System.out.println("Got initial response");
        System.out.println(response);

        Thread.sleep(1800000);
        System.out.println("Closing connection");
        connection.close();
    }

}
