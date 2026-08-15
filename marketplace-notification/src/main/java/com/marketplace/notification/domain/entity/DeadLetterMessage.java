package com.marketplace.notification.domain.entity;

import com.marketplace.notification.domain.valueobject.NotificationChannel;
import com.marketplace.notification.domain.valueobject.NotificationType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeadLetterMessage {

    private Long id;
    private Long notificationId;
    private NotificationType type;
    private NotificationChannel channel;
    private String recipientEmail;
    private String recipientPhone;
    private String subject;
    private String body;
    private String errorMessage;
    private String stackTrace;
    private String kafkaTopic;
    private String kafkaPartition;
    private String kafkaOffset;
    private String kafkaKey;
    private String originalPayload;
    private boolean resolved;
    private Long resolvedBy;
    private Instant resolvedAt;
    private String resolutionNotes;
    private Instant createdAt;
    private Instant updatedAt;

    public DeadLetterMessage(Long notificationId, NotificationType type, NotificationChannel channel,
                            String recipientEmail, String subject, String body, String errorMessage,
                            String stackTrace, String kafkaTopic, String kafkaPartition, String kafkaOffset,
                            String kafkaKey, String originalPayload) {
        this.notificationId = notificationId;
        this.type = type;
        this.channel = channel;
        this.recipientEmail = recipientEmail;
        this.subject = subject;
        this.body = body;
        this.errorMessage = errorMessage;
        this.stackTrace = stackTrace;
        this.kafkaTopic = kafkaTopic;
        this.kafkaPartition = kafkaPartition;
        this.kafkaOffset = kafkaOffset;
        this.kafkaKey = kafkaKey;
        this.originalPayload = originalPayload;
        this.resolved = false;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void resolve(Long resolvedBy, String resolutionNotes) {
        this.resolved = true;
        this.resolvedBy = resolvedBy;
        this.resolvedAt = Instant.now();
        this.resolutionNotes = resolutionNotes;
        this.updatedAt = Instant.now();
    }
}