package com.marketplace.notification.infrastructure.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.notification.application.dto.CreateNotificationRequest;
import com.marketplace.notification.application.service.NotificationService;
import com.marketplace.notification.domain.event.kafka.OrderShippedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderShippedEventConsumer {

    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
        topics = "order-events",
        groupId = "notification-order-shipped",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(ConsumerRecord<String, String> record, Acknowledgment ack) {
        try {
            log.info("Received order-shipped event: topic={}, partition={}, offset={}",
                record.topic(), record.partition(), record.offset());

            OrderShippedEvent event = objectMapper.readValue(record.value(), OrderShippedEvent.class);

            CreateNotificationRequest request = CreateNotificationRequest.builder()
                .referenceId(event.getEventId())
                .type("ORDER_SHIPPED")
                .channel("EMAIL")
                .priority("HIGH")
                .recipientId(event.getUserId())
                .recipientEmail(event.getEmail())
                .subject("Order Shipped - " + event.getOrderNumber())
                .body("Your order " + event.getOrderNumber() + " has been shipped. Tracking: " + event.getTrackingNumber())
                .templateCode("ORDER_SHIPPED")
                .templateVariables(java.util.Map.of(
                    "orderNumber", event.getOrderNumber(),
                    "trackingNumber", event.getTrackingNumber(),
                    "carrier", event.getCarrier(),
                    "estimatedDelivery", event.getEstimatedDelivery().toString()
                ))
                .build();

            notificationService.sendNotification(request);
            log.info("Order-shipped notification sent for order: {}", event.getOrderNumber());

            ack.acknowledge();
        } catch (Exception e) {
            log.error("Failed to process order-shipped event: {}", e.getMessage(), e);
            ack.acknowledge();
        }
    }
}