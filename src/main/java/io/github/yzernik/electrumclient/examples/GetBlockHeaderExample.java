package io.github.yzernik.electrumclient.examples;

import io.github.yzernik.electrumclient.ElectrumClient;

public class GetBlockHeaderExample {

    private static final String ELECTRUM_HOST = "electrum-server.ninja";
    private static final int ELECTRUM_PORT = 50001;

    public static void main(String[] args) throws Throwable {
        ElectrumClient electrumClient = new ElectrumClient(ELECTRUM_HOST, ELECTRUM_PORT);
        String hex = electrumClient.getHeader(631515);

        System.out.println("block header hex: " + hex);
    }

}
