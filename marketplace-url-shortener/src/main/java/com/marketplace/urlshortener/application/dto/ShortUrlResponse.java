package com.marketplace.urlshortener.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortUrlResponse {

    private Long id;
    private String shortCode;
    private String shortUrl;
    private String originalUrl;
    private String title;
    private String description;
    private String type;
    private String status;
    private String expirationType;
    private Instant expiresAt;
    private Long referenceId;
    private String referenceType;
    private Long createdBy;
    private boolean isCustomAlias;
    private long clickCount;
    private long uniqueClickCount;
    private Instant lastClickedAt;
    private boolean requiresPassword;
    private String tags;
    private Instant createdAt;

    public static ShortUrlResponse from(com.marketplace.urlshortener.domain.entity.ShortUrl shortUrl, String baseUrl) {
        return ShortUrlResponse.builder()
            .id(shortUrl.getId())
            .shortCode(shortUrl.getShortCode())
            .shortUrl(baseUrl + "/" + shortUrl.getShortCode())
            .originalUrl(shortUrl.getOriginalUrl())
            .title(shortUrl.getTitle())
            .description(shortUrl.getDescription())
            .type(shortUrl.getType().name())
            .status(shortUrl.getStatus().name())
            .expirationType(shortUrl.getExpirationType().name())
            .expiresAt(shortUrl.getExpiresAt())
            .referenceId(shortUrl.getReferenceId())
            .referenceType(shortUrl.getReferenceType())
            .createdBy(shortUrl.getCreatedBy())
            .isCustomAlias(shortUrl.isCustomAlias())
            .clickCount(shortUrl.getClickCount())
            .uniqueClickCount(shortUrl.getUniqueClickCount())
            .lastClickedAt(shortUrl.getLastClickedAt())
            .requiresPassword(shortUrl.isRequiresPassword())
            .tags(shortUrl.getTags())
            .createdAt(shortUrl.getCreatedAt())
            .build();
    }
}