package org.software.code.service.notification;

import org.software.code.model.message.NotificationMessage;
import org.software.code.kafaka.NotificationProducer;
import org.springframework.stereotype.Component;

@Component
public class EpidemicPreventionNotificationHandler implements NotificationHandler {

    private final NotificationProducer producer;

    public EpidemicPreventionNotificationHandler(NotificationProducer producer) {
        this.producer = producer;
    }

    @Override
    public void handle(NotificationMessage message) {
        message.setType("EPIDEMIC");
        producer.sendNotification("notification-topic", message);
    }
}