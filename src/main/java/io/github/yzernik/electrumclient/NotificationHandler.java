package io.github.yzernik.electrumclient;

public interface NotificationHandler<S extends ElectrumResponse> {
    public void handleNotification(S notification);
}
