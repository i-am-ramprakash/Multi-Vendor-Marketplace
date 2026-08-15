package com.marketplace.notification.infrastructure.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.notification.application.dto.CreateNotificationRequest;
import com.marketplace.notification.application.service.NotificationService;
import com.marketplace.notification.domain.event.kafka.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderCreatedEventConsumer {

    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
        topics = "order-events",
        groupId = "notification-order-created",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(ConsumerRecord<String, String> record, Acknowledgment ack) {
        try {
            log.info("Received order-created event: topic={}, partition={}, offset={}",
                record.topic(), record.partition(), record.offset());

            OrderCreatedEvent event = objectMapper.readValue(record.value(), OrderCreatedEvent.class);

            CreateNotificationRequest request = CreateNotificationRequest.builder()
                .referenceId(event.getEventId())
                .type("ORDER_CREATED")
                .channel("EMAIL")
                .priority("HIGH")
                .recipientId(event.getUserId())
                .recipientEmail(event.getEmail())
                .subject("Order Confirmation - " + event.getOrderNumber())
                .body("Your order " + event.getOrderNumber() + " has been placed successfully. Total: " + event.getTotalAmount() + " " + event.getCurrency())
                .templateCode("ORDER_CREATED")
                .templateVariables(java.util.Map.of(
                    "orderNumber", event.getOrderNumber(),
                    "totalAmount", event.getTotalAmount().toString(),
                    "currency", event.getCurrency(),
                    "itemCount", String.valueOf(event.getItemCount())
                ))
                .build();

            notificationService.sendNotification(request);
            log.info("Order-created notification sent for order: {}", event.getOrderNumber());

            ack.acknowledge();
        } catch (Exception e) {
            log.error("Failed to process order-created event: {}", e.getMessage(), e);
            ack.acknowledge();
        }
    }
}