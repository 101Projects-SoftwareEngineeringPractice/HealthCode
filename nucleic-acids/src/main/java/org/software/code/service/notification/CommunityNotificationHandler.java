package org.software.code.service.notification;

import org.software.code.dto.NotificationMessage;
import org.software.code.kafaka.NotificationProducer;
import org.springframework.stereotype.Component;

@Component
public class CommunityNotificationHandler implements NotificationHandler {

    private final NotificationProducer producer;

    public CommunityNotificationHandler(NotificationProducer producer) {
        this.producer = producer;
    }

    @Override
    public void handle(NotificationMessage message) {
        message.setType("COMMUNITY");
        producer.sendNotification("notification-topic", message);
    }
}