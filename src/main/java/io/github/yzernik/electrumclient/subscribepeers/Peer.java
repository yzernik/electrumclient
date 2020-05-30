package io.github.yzernik.electrumclient.subscribepeers;

public class Peer {

    public String ip;
    public String hostname;
    public PeerServerFeatures features;

    public Peer(String ip, String hostname, PeerServerFeatures features) {
        this.ip = ip;
        this.hostname = hostname;
        this.features = features;
    }

    @Override
    public String toString() {
        return "Peer(" +
                "ip: " + ip + ", " +
                "hostname: " + hostname + ", " +
                "features: " + features +
                ")";
    }

}
