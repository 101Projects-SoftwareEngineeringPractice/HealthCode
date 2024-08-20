package org.software.code.kafaka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.software.code.common.except.BusinessException;
import org.software.code.common.except.ExceptionEnum;
import org.software.code.model.message.NotificationMessage;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class NotificationConsumer {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LogManager.getLogger(NotificationConsumer.class);

    @KafkaListener(topics = "notification-topic", groupId = "notification-group")
    public void consumeNotification(String messageStr) {
        try {
            NotificationMessage message = objectMapper.readValue(messageStr, NotificationMessage.class);

            // 根据消息类型执行相应的通知操作
            switch (message.getType()) {
                case "SMS":
                    sendSmsNotification(message);
                    break;
                case "COMMUNITY":
                    reportToCommunity(message);
                    break;
                case "EPIDEMIC":
                    reportToEpidemicPrevention(message);
                    break;
                case "POSITIVE":
                    reportPositive(message);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ExceptionEnum.RETEST_NOTIFICATION_EXCEPTION);
        }
    }

    private void sendSmsNotification(NotificationMessage message) {
        // TODO 发送短信通知的逻辑
        logger.info("Sending SMS notification to user: Name={}, Identity Card={}, Phone={}", message.getName(), message.getIdentity_card(), message.getPhone());
    }

    private void reportToCommunity(NotificationMessage message) {
        // TODO 上报社区的逻辑
        logger.info("Reporting to community for user: Name={}, Identity Card={}, Phone={}", message.getName(), message.getIdentity_card(), message.getPhone());
    }

    private void reportToEpidemicPrevention(NotificationMessage message) {
        // TODO 上报疫情防控办的逻辑
        logger.info("Reporting to epidemic prevention office for user: Name={}, Identity Card={}, Phone={}", message.getName(), message.getIdentity_card(), message.getPhone());
    }

    private void reportPositive(NotificationMessage message) {
        // TODO 单管阳性
        logger.info("single tubei positive: Name={}, Identity Card={}, Phone={}", message.getName(), message.getIdentity_card(), message.getPhone());

    }
}