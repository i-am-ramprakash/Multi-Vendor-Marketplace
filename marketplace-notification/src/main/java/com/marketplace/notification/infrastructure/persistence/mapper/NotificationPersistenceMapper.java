package com.marketplace.notification.infrastructure.persistence.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.notification.domain.entity.Notification;
import com.marketplace.notification.domain.valueobject.*;
import com.marketplace.notification.infrastructure.persistence.entity.NotificationJpaEntity;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public final class NotificationPersistenceMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private NotificationPersistenceMapper() {}

    public static NotificationJpaEntity toJpaEntity(Notification domain) {
        if (domain == null) return null;

        NotificationJpaEntity jpa = new NotificationJpaEntity();
        jpa.setId(domain.getId());
        jpa.setReferenceId(domain.getReferenceId());
        jpa.setType(domain.getType());
        jpa.setChannel(domain.getChannel());
        jpa.setStatus(domain.getStatus());
        jpa.setPriority(domain.getPriority());
        jpa.setRecipientId(domain.getRecipientId());
        jpa.setRecipientEmail(domain.getRecipientEmail());
        jpa.setRecipientPhone(domain.getRecipientPhone());
        jpa.setSubject(domain.getSubject());
        jpa.setBody(domain.getBody());
        jpa.setTemplateCode(domain.getTemplateCode());
        jpa.setTemplateVariables(convertMapToJson(domain.getTemplateVariables()));
        jpa.setMetadata(domain.getMetadata());
        jpa.setRetryCount(domain.getRetryCount());
        jpa.setMaxRetries(domain.getMaxRetries());
        jpa.setNextRetryAt(domain.getNextRetryAt());
        jpa.setLastRetryAt(domain.getLastRetryAt());
        jpa.setSentAt(domain.getSentAt());
        jpa.setDeliveredAt(domain.getDeliveredAt());
        jpa.setFailedAt(domain.getFailedAt());
        jpa.setFailureReason(domain.getFailureReason());
        jpa.setKafkaTopic(domain.getKafkaTopic());
        jpa.setKafkaPartition(domain.getKafkaPartition());
        jpa.setKafkaOffset(domain.getKafkaOffset());
        jpa.setDeadLetterId(domain.getDeadLetterId());
        jpa.setCreatedAt(domain.getCreatedAt());
        jpa.setUpdatedAt(domain.getUpdatedAt());

        return jpa;
    }

    public static Notification toDomain(NotificationJpaEntity jpa) {
        if (jpa == null) return null;

        Notification notification = new Notification(
            jpa.getReferenceId(),
            jpa.getType(),
            jpa.getChannel(),
            jpa.getRecipientId(),
            jpa.getRecipientEmail(),
            jpa.getSubject(),
            jpa.getBody()
        );
        setId(notification, jpa.getId());
        notification.setStatus(jpa.getStatus());
        notification.setPriority(jpa.getPriority());
        notification.setRecipientPhone(jpa.getRecipientPhone());
        notification.setTemplateCode(jpa.getTemplateCode());
        notification.setTemplateVariables(convertJsonToMap(jpa.getTemplateVariables()));
        notification.setMetadata(jpa.getMetadata());
        notification.setRetryCount(jpa.getRetryCount());
        notification.setMaxRetries(jpa.getMaxRetries());
        notification.setNextRetryAt(jpa.getNextRetryAt());
        notification.setLastRetryAt(jpa.getLastRetryAt());
        notification.setSentAt(jpa.getSentAt());
        notification.setDeliveredAt(jpa.getDeliveredAt());
        notification.setFailedAt(jpa.getFailedAt());
        notification.setFailureReason(jpa.getFailureReason());
        notification.setKafkaTopic(jpa.getKafkaTopic());
        notification.setKafkaPartition(jpa.getKafkaPartition());
        notification.setKafkaOffset(jpa.getKafkaOffset());
        notification.setDeadLetterId(jpa.getDeadLetterId());
        notification.setCreatedAt(jpa.getCreatedAt());
        notification.setUpdatedAt(jpa.getUpdatedAt());

        return notification;
    }

    private static void setId(Notification notification, Long id) {
        try {
            Field field = Notification.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(notification, id);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot set notification ID", e);
        }
    }

    private static String convertMapToJson(Map<String, String> map) {
        if (map == null || map.isEmpty()) return null;
        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    private static Map<String, String> convertJsonToMap(String json) {
        if (json == null || json.isBlank()) return new HashMap<>();
        try {
            return objectMapper.readValue(json, HashMap.class);
        } catch (JsonProcessingException e) {
            return new HashMap<>();
        }
    }
}