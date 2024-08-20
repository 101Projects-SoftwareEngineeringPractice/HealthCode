package org.software.code.kafaka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.software.code.model.message.NotificationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void sendNotification(String topic, NotificationMessage message) {
        try {
            String messageStr = objectMapper.writeValueAsString(message);
            kafkaTemplate.send(topic, messageStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
