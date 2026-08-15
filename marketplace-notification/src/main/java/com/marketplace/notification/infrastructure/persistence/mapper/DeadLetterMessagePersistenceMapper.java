package com.marketplace.notification.infrastructure.persistence.mapper;

import com.marketplace.notification.domain.entity.DeadLetterMessage;
import com.marketplace.notification.infrastructure.persistence.entity.DeadLetterMessageJpaEntity;

import java.lang.reflect.Field;

public final class DeadLetterMessagePersistenceMapper {

    private DeadLetterMessagePersistenceMapper() {}

    public static DeadLetterMessageJpaEntity toJpaEntity(DeadLetterMessage domain) {
        if (domain == null) return null;

        DeadLetterMessageJpaEntity jpa = new DeadLetterMessageJpaEntity();
        jpa.setId(domain.getId());
        jpa.setNotificationId(domain.getNotificationId());
        jpa.setType(domain.getType());
        jpa.setChannel(domain.getChannel());
        jpa.setRecipientEmail(domain.getRecipientEmail());
        jpa.setRecipientPhone(domain.getRecipientPhone());
        jpa.setSubject(domain.getSubject());
        jpa.setBody(domain.getBody());
        jpa.setErrorMessage(domain.getErrorMessage());
        jpa.setStackTrace(domain.getStackTrace());
        jpa.setKafkaTopic(domain.getKafkaTopic());
        jpa.setKafkaPartition(domain.getKafkaPartition());
        jpa.setKafkaOffset(domain.getKafkaOffset());
        jpa.setKafkaKey(domain.getKafkaKey());
        jpa.setOriginalPayload(domain.getOriginalPayload());
        jpa.setResolved(domain.isResolved());
        jpa.setResolvedBy(domain.getResolvedBy());
        jpa.setResolvedAt(domain.getResolvedAt());
        jpa.setResolutionNotes(domain.getResolutionNotes());
        jpa.setCreatedAt(domain.getCreatedAt());
        jpa.setUpdatedAt(domain.getUpdatedAt());

        return jpa;
    }

    public static DeadLetterMessage toDomain(DeadLetterMessageJpaEntity jpa) {
        if (jpa == null) return null;

        DeadLetterMessage deadLetter = new DeadLetterMessage(
            jpa.getNotificationId(),
            jpa.getType(),
            jpa.getChannel(),
            jpa.getRecipientEmail(),
            jpa.getSubject(),
            jpa.getBody(),
            jpa.getErrorMessage(),
            jpa.getStackTrace(),
            jpa.getKafkaTopic(),
            jpa.getKafkaPartition(),
            jpa.getKafkaOffset(),
            jpa.getKafkaKey(),
            jpa.getOriginalPayload()
        );
        setId(deadLetter, jpa.getId());
        deadLetter.setResolved(jpa.isResolved());
        deadLetter.setResolvedBy(jpa.getResolvedBy());
        deadLetter.setResolvedAt(jpa.getResolvedAt());
        deadLetter.setResolutionNotes(jpa.getResolutionNotes());
        deadLetter.setCreatedAt(jpa.getCreatedAt());
        deadLetter.setUpdatedAt(jpa.getUpdatedAt());

        return deadLetter;
    }

    private static void setId(DeadLetterMessage deadLetter, Long id) {
        try {
            Field field = DeadLetterMessage.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(deadLetter, id);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot set dead letter message ID", e);
        }
    }
}