package com.marketplace.notification.infrastructure.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.notification.application.dto.CreateNotificationRequest;
import com.marketplace.notification.application.service.NotificationService;
import com.marketplace.notification.domain.event.kafka.ProductApprovedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProductApprovedEventConsumer {

    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
        topics = "product-events",
        groupId = "notification-product-approved",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(ConsumerRecord<String, String> record, Acknowledgment ack) {
        try {
            log.info("Received product-approved event: topic={}, partition={}, offset={}",
                record.topic(), record.partition(), record.offset());

            ProductApprovedEvent event = objectMapper.readValue(record.value(), ProductApprovedEvent.class);

            CreateNotificationRequest request = CreateNotificationRequest.builder()
                .referenceId(event.getEventId())
                .type("PRODUCT_APPROVED")
                .channel("EMAIL")
                .priority("NORMAL")
                .recipientId(event.getUserId())
                .recipientEmail(event.getEmail())
                .subject("Product Approved - " + event.getProductName())
                .body("Your product '" + event.getProductName() + "' has been approved and is now live.")
                .templateCode("PRODUCT_APPROVED")
                .templateVariables(java.util.Map.of(
                    "productName", event.getProductName(),
                    "vendorName", event.getVendorName(),
                    "price", event.getPrice().toString(),
                    "currency", event.getCurrency()
                ))
                .build();

            notificationService.sendNotification(request);
            log.info("Product-approved notification sent for product: {}", event.getProductId());

            ack.acknowledge();
        } catch (Exception e) {
            log.error("Failed to process product-approved event: {}", e.getMessage(), e);
            ack.acknowledge();
        }
    }
}