package io.github.yzernik.electrumclient.examples;

import io.github.yzernik.electrumclient.ElectrumClient;
import io.github.yzernik.electrumclient.NotificationHandler;
import io.github.yzernik.electrumclient.SubscribeHeadersResponse;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SubscribeBlockHeadersExample {

    // Public electrum server from https://1209k.com/bitcoin-eye/ele.php?chain=btc
    private static final String ELECTRUM_HOST = "electrumx-core.1209k.com";
    private static final int ELECTRUM_PORT = 50001;

    public static void main(String[] args) throws InterruptedException, ExecutionException, UnknownHostException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        InetSocketAddress address = new InetSocketAddress(ELECTRUM_HOST, ELECTRUM_PORT);
        ElectrumClient electrumClient = new ElectrumClient(address, executorService);
        NotificationHandler<SubscribeHeadersResponse> notificationHandler =
                notification -> System.out.println(notification);

        System.out.println("Calling subscribeHeaders.");
        Future<SubscribeHeadersResponse> responseFuture = electrumClient.subscribeHeaders(notificationHandler);
        System.out.println("Got response future.");
        Thread.sleep(10000);
        System.out.println("Done sleeping.");
        responseFuture.cancel(true);
        System.out.println("Canceled task.");

        executorService.shutdown();
        executorService.isTerminated();
        System.out.println("Shut down executorservice");
        System.out.println("executorService.isTerminated: " + executorService.isTerminated());
    }

}
