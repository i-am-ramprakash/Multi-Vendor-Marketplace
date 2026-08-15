package com.marketplace.notification.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeadLetterResponse {

    private Long id;
    private Long notificationId;
    private String type;
    private String channel;
    private String recipientEmail;
    private String subject;
    private String body;
    private String errorMessage;
    private String stackTrace;
    private String kafkaTopic;
    private String kafkaPartition;
    private String kafkaOffset;
    private boolean resolved;
    private Long resolvedBy;
    private Instant resolvedAt;
    private String resolutionNotes;
    private Instant createdAt;

    public static DeadLetterResponse from(com.marketplace.notification.domain.entity.DeadLetterMessage deadLetter) {
        return DeadLetterResponse.builder()
            .id(deadLetter.getId())
            .notificationId(deadLetter.getNotificationId())
            .type(deadLetter.getType().name())
            .channel(deadLetter.getChannel().name())
            .recipientEmail(deadLetter.getRecipientEmail())
            .subject(deadLetter.getSubject())
            .body(deadLetter.getBody())
            .errorMessage(deadLetter.getErrorMessage())
            .stackTrace(deadLetter.getStackTrace())
            .kafkaTopic(deadLetter.getKafkaTopic())
            .kafkaPartition(deadLetter.getKafkaPartition())
            .kafkaOffset(deadLetter.getKafkaOffset())
            .resolved(deadLetter.isResolved())
            .resolvedBy(deadLetter.getResolvedBy())
            .resolvedAt(deadLetter.getResolvedAt())
            .resolutionNotes(deadLetter.getResolutionNotes())
            .createdAt(deadLetter.getCreatedAt())
            .build();
    }
}