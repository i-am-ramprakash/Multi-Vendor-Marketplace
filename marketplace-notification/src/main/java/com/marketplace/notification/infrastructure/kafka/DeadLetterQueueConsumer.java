package com.marketplace.notification.infrastructure.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.notification.domain.entity.DeadLetterMessage;
import com.marketplace.notification.domain.entity.Notification;
import com.marketplace.notification.domain.repository.DeadLetterMessageRepository;
import com.marketplace.notification.domain.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class DeadLetterQueueConsumer {

    private final DeadLetterMessageRepository deadLetterMessageRepository;
    private final NotificationRepository notificationRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(
        topics = "notification-events-dlq",
        groupId = "notification-dlq-processor",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(ConsumerRecord<String, String> record, Acknowledgment ack) {
        try {
            log.info("Received dead letter message: topic={}, partition={}, offset={}",
                record.topic(), record.partition(), record.offset());

            Notification notification = objectMapper.readValue(record.value(), Notification.class);

            // Find notification in database
            java.util.Optional<Notification> existingNotification = notificationRepository.findByReferenceId(
                notification.getReferenceId() != null ? notification.getReferenceId() : String.valueOf(notification.getId())
            );

            if (existingNotification.isEmpty()) {
                log.warn("Notification not found in database for dead letter: {}", notification.getReferenceId());
                ack.acknowledge();
                return;
            }

            Notification dbNotification = existingNotification.get();

            // Create dead letter message
            DeadLetterMessage deadLetterMessage = new DeadLetterMessage(
                dbNotification.getId(),
                dbNotification.getType(),
                dbNotification.getChannel(),
                dbNotification.getRecipientEmail(),
                dbNotification.getSubject(),
                dbNotification.getBody(),
                "Notification failed after max retries",
                null,
                record.topic(),
                String.valueOf(record.partition()),
                String.valueOf(record.offset()),
                record.key(),
                record.value()
            );

            DeadLetterMessage savedDeadLetter = deadLetterMessageRepository.save(deadLetterMessage);

            // Update notification with dead letter reference
            dbNotification.moveToDeadLetter(savedDeadLetter.getId());
            notificationRepository.save(dbNotification);

            log.info("Dead letter message created for notification: {}", dbNotification.getId());

            ack.acknowledge();

        } catch (Exception e) {
            log.error("Failed to process dead letter message: {}", e.getMessage(), e);
            ack.acknowledge();
        }
    }
}