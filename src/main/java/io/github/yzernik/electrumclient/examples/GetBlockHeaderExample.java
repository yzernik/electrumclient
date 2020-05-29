package io.github.yzernik.electrumclient.examples;

import io.github.yzernik.electrumclient.ElectrumClient;
import io.github.yzernik.electrumclient.GetHeaderClientConnection;
import io.github.yzernik.electrumclient.GetHeaderResponse;

public class GetBlockHeaderExample {

    // Public electrum server from https://1209k.com/bitcoin-eye/ele.php?chain=btc
    private static final String ELECTRUM_HOST = "electrumx-core.1209k.com";
    private static final int ELECTRUM_PORT = 50001;

    public static void main(String[] args) throws Exception {
        ElectrumClient electrumClient = new ElectrumClient(ELECTRUM_HOST, ELECTRUM_PORT);
        GetHeaderClientConnection connection = electrumClient.getHeader(631515);

        GetHeaderResponse response = connection.getResult();
        System.out.println("block header hex: " + response.hex);
    }

}
