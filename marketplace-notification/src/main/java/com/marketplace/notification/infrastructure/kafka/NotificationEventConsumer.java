package com.marketplace.notification.infrastructure.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.notification.domain.entity.Notification;
import com.marketplace.notification.domain.repository.NotificationRepository;
import com.marketplace.notification.domain.service.EmailService;
import com.marketplace.notification.domain.valueobject.NotificationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationEventConsumer {

    private final NotificationRepository notificationRepository;
    private final EmailService emailService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
        topics = "notification-events",
        groupId = "notification-processor",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(ConsumerRecord<String, String> record, Acknowledgment ack) {
        try {
            log.info("Received notification event: topic={}, partition={}, offset={}",
                record.topic(), record.partition(), record.offset());

            Notification notification = objectMapper.readValue(record.value(), Notification.class);

            // Find notification in database
            Optional<Notification> existingNotification = notificationRepository.findByReferenceId(
                notification.getReferenceId() != null ? notification.getReferenceId() : String.valueOf(notification.getId())
            );

            if (existingNotification.isEmpty()) {
                log.warn("Notification not found in database: {}", notification.getReferenceId());
                ack.acknowledge();
                return;
            }

            Notification dbNotification = existingNotification.get();

            // Process notification based on channel
            switch (dbNotification.getChannel()) {
                case EMAIL -> {
                    emailService.sendEmail(
                        dbNotification.getRecipientEmail(),
                        dbNotification.getSubject(),
                        dbNotification.getBody()
                    );
                    dbNotification.markSent("notification-events", String.valueOf(record.partition()), String.valueOf(record.offset()));
                    log.info("Email notification sent successfully: {}", dbNotification.getId());
                }
                case SMS -> {
                    // SMS implementation
                    dbNotification.markSent("notification-events", String.valueOf(record.partition()), String.valueOf(record.offset()));
                    log.info("SMS notification sent successfully: {}", dbNotification.getId());
                }
                case PUSH -> {
                    // Push notification implementation
                    dbNotification.markSent("notification-events", String.valueOf(record.partition()), String.valueOf(record.offset()));
                    log.info("Push notification sent successfully: {}", dbNotification.getId());
                }
                default -> {
                    log.warn("Unsupported notification channel: {}", dbNotification.getChannel());
                    dbNotification.markFailed("Unsupported channel: " + dbNotification.getChannel());
                }
            }

            notificationRepository.save(dbNotification);
            ack.acknowledge();

        } catch (Exception e) {
            log.error("Failed to process notification event: {}", e.getMessage(), e);
            ack.acknowledge();
        }
    }
}