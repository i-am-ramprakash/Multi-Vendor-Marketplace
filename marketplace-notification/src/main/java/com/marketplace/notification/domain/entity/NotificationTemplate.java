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
public class NotificationTemplate {

    private Long id;
    private String code;
    private String name;
    private String description;
    private NotificationType type;
    private NotificationChannel channel;
    private String subjectTemplate;
    private String bodyTemplate;
    private String htmlTemplate;
    private boolean isActive;
    private String locale;
    private String version;
    private Instant createdAt;
    private Instant updatedAt;

    public NotificationTemplate(String code, String name, NotificationType type,
                               NotificationChannel channel, String subjectTemplate, String bodyTemplate) {
        this.code = code;
        this.name = name;
        this.type = type;
        this.channel = channel;
        this.subjectTemplate = subjectTemplate;
        this.bodyTemplate = bodyTemplate;
        this.isActive = true;
        this.locale = "en";
        this.version = "1.0";
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void activate() {
        this.isActive = true;
        this.updatedAt = Instant.now();
    }

    public void deactivate() {
        this.isActive = false;
        this.updatedAt = Instant.now();
    }
}