package com.marketplace.notification.infrastructure.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.notification.application.dto.CreateNotificationRequest;
import com.marketplace.notification.application.service.NotificationService;
import com.marketplace.notification.domain.event.kafka.OrderDeliveredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderDeliveredEventConsumer {

    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
        topics = "order-events",
        groupId = "notification-order-delivered",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(ConsumerRecord<String, String> record, Acknowledgment ack) {
        try {
            log.info("Received order-delivered event: topic={}, partition={}, offset={}",
                record.topic(), record.partition(), record.offset());

            OrderDeliveredEvent event = objectMapper.readValue(record.value(), OrderDeliveredEvent.class);

            CreateNotificationRequest request = CreateNotificationRequest.builder()
                .referenceId(event.getEventId())
                .type("ORDER_DELIVERED")
                .channel("EMAIL")
                .priority("NORMAL")
                .recipientId(event.getUserId())
                .recipientEmail(event.getEmail())
                .subject("Order Delivered - " + event.getOrderNumber())
                .body("Your order " + event.getOrderNumber() + " has been delivered successfully.")
                .templateCode("ORDER_DELIVERED")
                .templateVariables(java.util.Map.of(
                    "orderNumber", event.getOrderNumber(),
                    "totalAmount", event.getTotalAmount().toString(),
                    "currency", event.getCurrency(),
                    "deliveredAt", event.getDeliveredAt().toString()
                ))
                .build();

            notificationService.sendNotification(request);
            log.info("Order-delivered notification sent for order: {}", event.getOrderNumber());

            ack.acknowledge();
        } catch (Exception e) {
            log.error("Failed to process order-delivered event: {}", e.getMessage(), e);
            ack.acknowledge();
        }
    }
}