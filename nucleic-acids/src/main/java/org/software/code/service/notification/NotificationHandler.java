package org.software.code.service.notification;

import org.software.code.dto.NotificationMessage;

public interface NotificationHandler {
    void handle(NotificationMessage message);
}