package com.marketplace.notification.infrastructure.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.notification.application.dto.CreateNotificationRequest;
import com.marketplace.notification.application.service.NotificationService;
import com.marketplace.notification.domain.event.kafka.VendorApprovedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class VendorApprovedEventConsumer {

    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
        topics = "vendor-events",
        groupId = "notification-vendor-approved",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(ConsumerRecord<String, String> record, Acknowledgment ack) {
        try {
            log.info("Received vendor-approved event: topic={}, partition={}, offset={}",
                record.topic(), record.partition(), record.offset());

            VendorApprovedEvent event = objectMapper.readValue(record.value(), VendorApprovedEvent.class);

            CreateNotificationRequest request = CreateNotificationRequest.builder()
                .referenceId(event.getEventId())
                .type("VENDOR_APPROVED")
                .channel("EMAIL")
                .priority("HIGH")
                .recipientId(event.getUserId())
                .recipientEmail(event.getEmail())
                .subject("Vendor Account Approved!")
                .body("Congratulations! Your vendor account " + event.getStoreName() + " has been approved.")
                .templateCode("VENDOR_APPROVED")
                .templateVariables(java.util.Map.of(
                    "vendorName", event.getVendorName(),
                    "storeName", event.getStoreName(),
                    "email", event.getEmail()
                ))
                .build();

            notificationService.sendNotification(request);
            log.info("Vendor-approved notification sent for vendor: {}", event.getVendorId());

            ack.acknowledge();
        } catch (Exception e) {
            log.error("Failed to process vendor-approved event: {}", e.getMessage(), e);
            ack.acknowledge();
        }
    }
}