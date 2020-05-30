package io.github.yzernik.electrumclient;

import io.github.yzernik.electrumclient.exceptions.ElectrumRPCParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetSocketAddress;

abstract public class ElectrumClientSubscribeConnection<T extends ElectrumResponse> extends ElectrumClientConnection<T> {

    private final static int DEFAULT_SUBSCRIBE_SOCKET_TIMEOUT = 0;

    private NotificationHandler<T> notificationHandler;

    public ElectrumClientSubscribeConnection(InetSocketAddress address, NotificationHandler<T> notificationHandler) {
        super(address, DEFAULT_SUBSCRIBE_SOCKET_TIMEOUT);
        this.notificationHandler = notificationHandler;
    }

    public ElectrumClientSubscribeConnection(InetSocketAddress address, NotificationHandler<T> notificationHandler, int socketTimeout) {
        super(address, socketTimeout);
        this.notificationHandler = notificationHandler;
    }

    abstract T parseNotification(String line, ElectrumRPCClient electrumRPCClient) throws ElectrumRPCParseException;

    void handleNotifications(BufferedReader in, ElectrumRPCClient electrumRPCClient) throws IOException, ElectrumRPCParseException {
        System.out.println("Handling notification lines...");
        String notificationLine = in.readLine();
        while (true) {
            System.out.println("Handling notification line step 1: " + notificationLine);
            if (Thread.interrupted()) {
                return;
            }
            System.out.println("Handling notification line step 2: " + notificationLine);
            if (notificationLine == null) {
                throw new ElectrumRPCParseException("Null notification line.");
            }
            T notification = parseNotification(notificationLine, electrumRPCClient);
            System.out.println("Handling notification: " + notification);
            notificationHandler.handleNotification(notification);
            notificationLine = in.readLine();
        }
    }

    @Override
    T getResponse(BufferedReader in, ElectrumRPCClient electrumRPCClient) throws IOException, ElectrumRPCParseException {
        T firstResponse = super.getResponse(in, electrumRPCClient);
        notificationHandler.handleNotification(firstResponse);
        handleNotifications(in, electrumRPCClient);
        return null;
    }

}
