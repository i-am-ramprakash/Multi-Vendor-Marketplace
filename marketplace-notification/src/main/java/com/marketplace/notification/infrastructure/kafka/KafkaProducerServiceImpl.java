package com.marketplace.notification.infrastructure.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.notification.domain.entity.Notification;
import com.marketplace.notification.domain.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducerServiceImpl implements KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String NOTIFICATION_TOPIC = "notification-events";

    @Override
    public void sendNotification(Notification notification) {
        try {
            String payload = objectMapper.writeValueAsString(notification);
            String key = notification.getReferenceId() != null ?
                notification.getReferenceId() : String.valueOf(notification.getId());

            ProducerRecord<String, String> record = new ProducerRecord<>(NOTIFICATION_TOPIC, key, payload);
            record.headers()
                .add(new RecordHeader("notification-type", notification.getType().name().getBytes(StandardCharsets.UTF_8)))
                .add(new RecordHeader("notification-channel", notification.getChannel().name().getBytes(StandardCharsets.UTF_8)))
                .add(new RecordHeader("notification-priority", notification.getPriority().name().getBytes(StandardCharsets.UTF_8)));

            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(record);

            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Failed to send notification to Kafka: {}", ex.getMessage(), ex);
                } else {
                    log.info("Notification sent to Kafka: topic={}, partition={}, offset={}",
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
                }
            });
        } catch (Exception e) {
            log.error("Error serializing notification: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to send notification to Kafka", e);
        }
    }

    @Override
    public void sendNotificationWithKey(String key, Notification notification) {
        try {
            String payload = objectMapper.writeValueAsString(notification);

            ProducerRecord<String, String> record = new ProducerRecord<>(NOTIFICATION_TOPIC, key, payload);
            record.headers()
                .add(new RecordHeader("notification-type", notification.getType().name().getBytes(StandardCharsets.UTF_8)))
                .add(new RecordHeader("notification-channel", notification.getChannel().name().getBytes(StandardCharsets.UTF_8)));

            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(record);

            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Failed to send notification with key {}: {}", key, ex.getMessage(), ex);
                } else {
                    log.info("Notification sent with key {}: topic={}, partition={}, offset={}",
                        key,
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
                }
            });
        } catch (Exception e) {
            log.error("Error serializing notification with key {}: {}", key, e.getMessage(), e);
            throw new RuntimeException("Failed to send notification to Kafka with key: " + key, e);
        }
    }

    @Override
    public void sendToTopic(String topic, String key, String payload) {
        try {
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, payload);

            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(record);

            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Failed to send message to topic {}: {}", topic, ex.getMessage(), ex);
                } else {
                    log.info("Message sent to topic {}: partition={}, offset={}",
                        topic,
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
                }
            });
        } catch (Exception e) {
            log.error("Error sending message to topic {}: {}", topic, e.getMessage(), e);
            throw new RuntimeException("Failed to send message to topic: " + topic, e);
        }
    }
}