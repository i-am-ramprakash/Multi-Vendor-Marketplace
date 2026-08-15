package com.marketplace.notification.domain.entity;

import com.marketplace.notification.domain.valueobject.NotificationChannel;
import com.marketplace.notification.domain.valueobject.NotificationPriority;
import com.marketplace.notification.domain.valueobject.NotificationStatus;
import com.marketplace.notification.domain.valueobject.NotificationType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {

    private Long id;
    private String referenceId;
    private NotificationType type;
    private NotificationChannel channel;
    private NotificationStatus status;
    private NotificationPriority priority;
    private Long recipientId;
    private String recipientEmail;
    private String recipientPhone;
    private String subject;
    private String body;
    private String templateCode;
    private Map<String, String> templateVariables;
    private String metadata;
    private int retryCount;
    private int maxRetries;
    private Instant nextRetryAt;
    private Instant lastRetryAt;
    private Instant sentAt;
    private Instant deliveredAt;
    private Instant failedAt;
    private String failureReason;
    private String kafkaTopic;
    private String kafkaPartition;
    private String kafkaOffset;
    private Long deadLetterId;
    private Instant createdAt;
    private Instant updatedAt;

    public Notification(String referenceId, NotificationType type, NotificationChannel channel,
                       Long recipientId, String recipientEmail, String subject, String body) {
        this.referenceId = referenceId;
        this.type = type;
        this.channel = channel;
        this.status = NotificationStatus.PENDING;
        this.priority = NotificationPriority.NORMAL;
        this.recipientId = recipientId;
        this.recipientEmail = recipientEmail;
        this.subject = subject;
        this.body = body;
        this.templateVariables = new HashMap<>();
        this.retryCount = 0;
        this.maxRetries = 3;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void queue() {
        if (!status.canTransitionTo(NotificationStatus.QUEUED)) {
            throw new IllegalStateException("Cannot queue notification in " + status + " status");
        }
        this.status = NotificationStatus.QUEUED;
        this.updatedAt = Instant.now();
    }

    public void process() {
        if (!status.canTransitionTo(NotificationStatus.PROCESSING)) {
            throw new IllegalStateException("Cannot process notification in " + status + " status");
        }
        this.status = NotificationStatus.PROCESSING;
        this.updatedAt = Instant.now();
    }

    public void markSent(String kafkaTopic, String kafkaPartition, String kafkaOffset) {
        if (!status.canTransitionTo(NotificationStatus.SENT)) {
            throw new IllegalStateException("Cannot mark notification as sent in " + status + " status");
        }
        this.status = NotificationStatus.SENT;
        this.sentAt = Instant.now();
        this.kafkaTopic = kafkaTopic;
        this.kafkaPartition = kafkaPartition;
        this.kafkaOffset = kafkaOffset;
        this.updatedAt = Instant.now();
    }

    public void markDelivered() {
        if (!status.canTransitionTo(NotificationStatus.DELIVERED)) {
            throw new IllegalStateException("Cannot mark notification as delivered in " + status + " status");
        }
        this.status = NotificationStatus.DELIVERED;
        this.deliveredAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void markFailed(String reason) {
        if (!status.canTransitionTo(NotificationStatus.FAILED)) {
            throw new IllegalStateException("Cannot mark notification as failed in " + status + " status");
        }
        this.status = NotificationStatus.FAILED;
        this.failureReason = reason;
        this.failedAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void retry() {
        if (!status.canTransitionTo(NotificationStatus.RETRYING)) {
            throw new IllegalStateException("Cannot retry notification in " + status + " status");
        }
        this.status = NotificationStatus.RETRYING;
        this.retryCount++;
        this.lastRetryAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void moveToDeadLetter(Long deadLetterId) {
        if (!status.canTransitionTo(NotificationStatus.DEAD_LETTER)) {
            throw new IllegalStateException("Cannot move notification to dead letter in " + status + " status");
        }
        this.status = NotificationStatus.DEAD_LETTER;
        this.deadLetterId = deadLetterId;
        this.updatedAt = Instant.now();
    }

    public void cancel() {
        if (!status.canTransitionTo(NotificationStatus.CANCELLED)) {
            throw new IllegalStateException("Cannot cancel notification in " + status + " status");
        }
        this.status = NotificationStatus.CANCELLED;
        this.updatedAt = Instant.now();
    }

    public boolean canBeRetried() {
        return retryCount < maxRetries && (status == NotificationStatus.FAILED || status == NotificationStatus.RETRYING);
    }

    public boolean isExpired() {
        if (nextRetryAt == null) return false;
        return Instant.now().isAfter(nextRetryAt);
    }

    public void setTemplateVariable(String key, String value) {
        if (this.templateVariables == null) {
            this.templateVariables = new HashMap<>();
        }
        this.templateVariables.put(key, value);
    }
}