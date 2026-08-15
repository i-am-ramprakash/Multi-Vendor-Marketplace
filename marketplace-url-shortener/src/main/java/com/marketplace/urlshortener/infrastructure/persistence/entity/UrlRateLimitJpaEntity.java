package com.marketplace.urlshortener.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "url_rate_limits", indexes = {
    @Index(name = "idx_url_rate_limits_identifier", columnList = "identifier, type", unique = true)
})
@Data
@NoArgsConstructor
public class UrlRateLimitJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "identifier", nullable = false, length = 255)
    private String identifier;

    @Column(name = "type", nullable = false, length = 50)
    private String type;

    @Column(name = "request_count", nullable = false)
    private int requestCount;

    @Column(name = "max_requests", nullable = false)
    private int maxRequests;

    @Column(name = "window_start", nullable = false)
    private Instant windowStart;

    @Column(name = "window_end", nullable = false)
    private Instant windowEnd;

    @Column(name = "is_blocked", nullable = false)
    private boolean isBlocked;

    @Column(name = "blocked_until")
    private Instant blockedUntil;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}