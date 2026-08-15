package com.marketplace.notification.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {

    private Long id;
    private String referenceId;
    private String type;
    private String channel;
    private String status;
    private String priority;
    private Long recipientId;
    private String recipientEmail;
    private String subject;
    private String body;
    private String templateCode;
    private Map<String, String> templateVariables;
    private int retryCount;
    private int maxRetries;
    private Instant nextRetryAt;
    private Instant sentAt;
    private Instant deliveredAt;
    private Instant failedAt;
    private String failureReason;
    private Instant createdAt;
    private Instant updatedAt;

    public static NotificationResponse from(com.marketplace.notification.domain.entity.Notification notification) {
        return NotificationResponse.builder()
            .id(notification.getId())
            .referenceId(notification.getReferenceId())
            .type(notification.getType().name())
            .channel(notification.getChannel().name())
            .status(notification.getStatus().name())
            .priority(notification.getPriority().name())
            .recipientId(notification.getRecipientId())
            .recipientEmail(notification.getRecipientEmail())
            .subject(notification.getSubject())
            .body(notification.getBody())
            .templateCode(notification.getTemplateCode())
            .templateVariables(notification.getTemplateVariables())
            .retryCount(notification.getRetryCount())
            .maxRetries(notification.getMaxRetries())
            .nextRetryAt(notification.getNextRetryAt())
            .sentAt(notification.getSentAt())
            .deliveredAt(notification.getDeliveredAt())
            .failedAt(notification.getFailedAt())
            .failureReason(notification.getFailureReason())
            .createdAt(notification.getCreatedAt())
            .updatedAt(notification.getUpdatedAt())
            .build();
    }
}