package io.github.yzernik.electrumclient.examples;

import io.github.yzernik.electrumclient.ElectrumClient;
import io.github.yzernik.electrumclient.GetHeaderResponse;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GetBlockHeaderExample {

    // Public electrum server from https://1209k.com/bitcoin-eye/ele.php?chain=btc
    private static final String ELECTRUM_HOST = "electrumx-core.1209k.com";
    private static final int ELECTRUM_PORT = 50001;

    public static void main(String[] args) throws ExecutionException, InterruptedException, UnknownHostException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        InetSocketAddress address = new InetSocketAddress(ELECTRUM_HOST, ELECTRUM_PORT);
        ElectrumClient electrumClient = new ElectrumClient(address, executorService);
        Future<GetHeaderResponse> responseFuture = electrumClient.getHeader(631515);

        GetHeaderResponse response = responseFuture.get();
        System.out.println("block header hex: " + response.hex);
        System.out.println("is task done: " + responseFuture.isDone());

        System.out.println("Shutting down executorservice...");
        executorService.shutdown();
        executorService.isTerminated();
        System.out.println("Shut down executorservice");
        System.out.println("executorService.isTerminated: " + executorService.isTerminated());
    }

}
