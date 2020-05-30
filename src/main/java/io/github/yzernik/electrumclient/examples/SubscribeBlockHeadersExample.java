package io.github.yzernik.electrumclient.examples;

import io.github.yzernik.electrumclient.*;
import io.github.yzernik.electrumclient.exceptions.ElectrumClientException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class SubscribeBlockHeadersExample {

    // Public electrum server from https://1209k.com/bitcoin-eye/ele.php?chain=btc
    private static final String ELECTRUM_HOST = "electrumx-core.1209k.com";
    private static final int ELECTRUM_PORT = 50001;

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ElectrumClient electrumClient = new ElectrumClient(ELECTRUM_HOST, ELECTRUM_PORT);
        NotificationHandler<SubscribeHeadersResponse> notificationHandler =
                notification -> System.out.println(notification);

        System.out.println("Calling subscribeHeaders.");
        Future<SubscribeHeadersResponse> responseFuture = electrumClient.subscribeHeaders(notificationHandler);
        System.out.println("Got response future.");
        Thread.sleep(10000);
        responseFuture.cancel(true);
    }

}
