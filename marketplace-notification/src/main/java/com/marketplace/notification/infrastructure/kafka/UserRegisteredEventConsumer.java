package com.marketplace.notification.infrastructure.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.notification.application.dto.CreateNotificationRequest;
import com.marketplace.notification.application.service.NotificationService;
import com.marketplace.notification.domain.event.kafka.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserRegisteredEventConsumer {

    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
        topics = "user-events",
        groupId = "notification-user-registered",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(ConsumerRecord<String, String> record, Acknowledgment ack) {
        try {
            log.info("Received user-registered event: topic={}, partition={}, offset={}",
                record.topic(), record.partition(), record.offset());

            UserRegisteredEvent event = objectMapper.readValue(record.value(), UserRegisteredEvent.class);

            CreateNotificationRequest request = CreateNotificationRequest.builder()
                .referenceId(event.getEventId())
                .type("USER_REGISTERED")
                .channel("EMAIL")
                .priority("NORMAL")
                .recipientId(event.getUserId())
                .recipientEmail(event.getEmail())
                .subject("Welcome to Our Marketplace!")
                .body("Welcome " + event.getFirstName() + "! Your account has been created successfully.")
                .templateCode("USER_REGISTERED")
                .templateVariables(java.util.Map.of(
                    "firstName", event.getFirstName(),
                    "lastName", event.getLastName(),
                    "email", event.getEmail(),
                    "role", event.getRole()
                ))
                .build();

            notificationService.sendNotification(request);
            log.info("User-registered notification sent for user: {}", event.getUserId());

            ack.acknowledge();
        } catch (Exception e) {
            log.error("Failed to process user-registered event: {}", e.getMessage(), e);
            ack.acknowledge();
        }
    }
}