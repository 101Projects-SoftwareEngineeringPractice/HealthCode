package org.software.code.service.notification;

import org.software.code.model.message.NotificationMessage;

import java.util.ArrayList;
import java.util.List;

public class NotificationChain {
    private List<NotificationHandler> handlers = new ArrayList<>();

    public NotificationChain addHandler(NotificationHandler handler) {
        handlers.add(handler);
        return this;
    }

    public void execute(NotificationMessage message) {
        for (NotificationHandler handler : handlers) {
            handler.handle(message);
        }
    }
}