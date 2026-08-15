package com.marketplace.urlshortener.infrastructure.persistence.entity;

import com.marketplace.urlshortener.domain.valueobject.ExpirationType;
import com.marketplace.urlshortener.domain.valueobject.UrlStatus;
import com.marketplace.urlshortener.domain.valueobject.UrlType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "short_urls", indexes = {
    @Index(name = "idx_short_urls_short_code", columnList = "short_code", unique = true),
    @Index(name = "idx_short_urls_created_by", columnList = "created_by"),
    @Index(name = "idx_short_urls_type", columnList = "type"),
    @Index(name = "idx_short_urls_status", columnList = "status"),
    @Index(name = "idx_short_urls_reference", columnList = "reference_id, reference_type"),
    @Index(name = "idx_short_urls_created_at", columnList = "created_at")
})
@Data
@NoArgsConstructor
public class ShortUrlJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "short_code", nullable = false, unique = true, length = 20)
    private String shortCode;

    @Column(name = "original_url", nullable = false, columnDefinition = "TEXT")
    private String originalUrl;

    @Column(name = "title", length = 500)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private UrlType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private UrlStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "expiration_type", nullable = false, length = 20)
    private ExpirationType expirationType;

    @Column(name = "expires_at")
    private Instant expiresAt;

    @Column(name = "reference_id")
    private Long referenceId;

    @Column(name = "reference_type", length = 50)
    private String referenceType;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "is_custom_alias", nullable = false)
    private boolean isCustomAlias;

    @Column(name = "click_count", nullable = false)
    private long clickCount;

    @Column(name = "unique_click_count", nullable = false)
    private long uniqueClickCount;

    @Column(name = "last_clicked_at")
    private Instant lastClickedAt;

    @Column(name = "password", length = 255)
    private String password;

    @Column(name = "requires_password", nullable = false)
    private boolean requiresPassword;

    @Column(name = "tags", columnDefinition = "TEXT")
    private String tags;

    @Column(name = "metadata", columnDefinition = "TEXT")
    private String metadata;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}