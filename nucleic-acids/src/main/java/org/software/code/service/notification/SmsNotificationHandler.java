package org.software.code.service.notification;

import org.software.code.model.message.NotificationMessage;
import org.software.code.kafaka.NotificationProducer;
import org.springframework.stereotype.Component;

@Component
public class SmsNotificationHandler implements NotificationHandler {

    private final NotificationProducer producer;

    public SmsNotificationHandler(NotificationProducer producer) {
        this.producer = producer;
    }

    @Override
    public void handle(NotificationMessage message) {
        message.setType("SMS");
        producer.sendNotification("notification-topic", message);
    }
}