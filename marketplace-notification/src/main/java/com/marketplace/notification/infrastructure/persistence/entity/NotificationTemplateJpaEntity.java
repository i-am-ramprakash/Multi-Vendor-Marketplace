package com.marketplace.notification.infrastructure.persistence.entity;

import com.marketplace.notification.domain.valueobject.NotificationChannel;
import com.marketplace.notification.domain.valueobject.NotificationType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "notification_templates", indexes = {
    @Index(name = "idx_notification_templates_code", columnList = "code", unique = true),
    @Index(name = "idx_notification_templates_type", columnList = "type"),
    @Index(name = "idx_notification_templates_channel", columnList = "channel"),
    @Index(name = "idx_notification_templates_is_active", columnList = "is_active")
})
@Data
@NoArgsConstructor
public class NotificationTemplateJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, unique = true, length = 100)
    private String code;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 30)
    private NotificationType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "channel", nullable = false, length = 20)
    private NotificationChannel channel;

    @Column(name = "subject_template", length = 500)
    private String subjectTemplate;

    @Column(name = "body_template", columnDefinition = "TEXT", nullable = false)
    private String bodyTemplate;

    @Column(name = "html_template", columnDefinition = "LONGTEXT")
    private String htmlTemplate;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "locale", length = 10)
    private String locale;

    @Column(name = "version", length = 20)
    private String version;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}