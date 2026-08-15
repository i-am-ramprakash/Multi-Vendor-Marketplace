package com.marketplace.notification.infrastructure.persistence.mapper;

import com.marketplace.notification.domain.entity.NotificationTemplate;
import com.marketplace.notification.infrastructure.persistence.entity.NotificationTemplateJpaEntity;

import java.lang.reflect.Field;

public final class NotificationTemplatePersistenceMapper {

    private NotificationTemplatePersistenceMapper() {}

    public static NotificationTemplateJpaEntity toJpaEntity(NotificationTemplate domain) {
        if (domain == null) return null;

        NotificationTemplateJpaEntity jpa = new NotificationTemplateJpaEntity();
        jpa.setId(domain.getId());
        jpa.setCode(domain.getCode());
        jpa.setName(domain.getName());
        jpa.setDescription(domain.getDescription());
        jpa.setType(domain.getType());
        jpa.setChannel(domain.getChannel());
        jpa.setSubjectTemplate(domain.getSubjectTemplate());
        jpa.setBodyTemplate(domain.getBodyTemplate());
        jpa.setHtmlTemplate(domain.getHtmlTemplate());
        jpa.setActive(domain.isActive());
        jpa.setLocale(domain.getLocale());
        jpa.setVersion(domain.getVersion());
        jpa.setCreatedAt(domain.getCreatedAt());
        jpa.setUpdatedAt(domain.getUpdatedAt());

        return jpa;
    }

    public static NotificationTemplate toDomain(NotificationTemplateJpaEntity jpa) {
        if (jpa == null) return null;

        NotificationTemplate template = new NotificationTemplate(
            jpa.getCode(),
            jpa.getName(),
            jpa.getType(),
            jpa.getChannel(),
            jpa.getSubjectTemplate(),
            jpa.getBodyTemplate()
        );
        setId(template, jpa.getId());
        template.setDescription(jpa.getDescription());
        template.setHtmlTemplate(jpa.getHtmlTemplate());
        template.setActive(jpa.isActive());
        template.setLocale(jpa.getLocale());
        template.setVersion(jpa.getVersion());
        template.setCreatedAt(jpa.getCreatedAt());
        template.setUpdatedAt(jpa.getUpdatedAt());

        return template;
    }

    private static void setId(NotificationTemplate template, Long id) {
        try {
            Field field = NotificationTemplate.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(template, id);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot set notification template ID", e);
        }
    }
}