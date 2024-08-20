package org.software.code.service.notification;

import org.software.code.model.message.NotificationMessage;

public interface NotificationHandler {
    void handle(NotificationMessage message);
}