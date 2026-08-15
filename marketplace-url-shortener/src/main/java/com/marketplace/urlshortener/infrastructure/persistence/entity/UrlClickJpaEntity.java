package com.marketplace.urlshortener.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "url_clicks", indexes = {
    @Index(name = "idx_url_clicks_short_url_id", columnList = "short_url_id"),
    @Index(name = "idx_url_clicks_short_code", columnList = "short_code"),
    @Index(name = "idx_url_clicks_clicked_at", columnList = "clicked_at"),
    @Index(name = "idx_url_clicks_ip_address", columnList = "ip_address")
})
@Data
@NoArgsConstructor
public class UrlClickJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "short_url_id", nullable = false)
    private Long shortUrlId;

    @Column(name = "short_code", nullable = false, length = 20)
    private String shortCode;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;

    @Column(name = "referer", columnDefinition = "TEXT")
    private String referer;

    @Column(name = "country", length = 100)
    private String country;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "device", length = 50)
    private String device;

    @Column(name = "browser", length = 50)
    private String browser;

    @Column(name = "os", length = 50)
    private String os;

    @Column(name = "is_unique", nullable = false)
    private boolean isUnique;

    @Column(name = "user_id")
    private Long userId;

    @CreationTimestamp
    @Column(name = "clicked_at", nullable = false, updatable = false)
    private Instant clickedAt;
}