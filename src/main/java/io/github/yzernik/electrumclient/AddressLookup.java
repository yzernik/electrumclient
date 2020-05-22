package io.github.yzernik.electrumclient;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class AddressLookup {

    public static InetAddress getInetAddress(String host) throws UnknownHostException {
        InetAddress[] addresses = InetAddress.getAllByName(host);
        return selectAddress(addresses);
    }

    private static InetAddress selectAddress(InetAddress[] addresses) {
        // Get ip6 address if available
        for (InetAddress addr : addresses) {
            if (addr instanceof Inet6Address) {
                return (Inet6Address) addr;
            }
        }

        // Get any address if available
        if(addresses.length > 0) {
            return addresses[0];
        }

        return null;
    }

}
