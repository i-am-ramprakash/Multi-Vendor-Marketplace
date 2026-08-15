package com.marketplace.urlshortener.domain.event;

import lombok.Getter;

@Getter
public class UrlCreatedEvent extends DomainEvent {

    private final Long shortUrlId;
    private final String shortCode;
    private final String originalUrl;
    private final String urlType;
    private final Long createdBy;

    public UrlCreatedEvent(Object source, Long shortUrlId, String shortCode, String originalUrl,
                          String urlType, Long createdBy) {
        super(source);
        this.shortUrlId = shortUrlId;
        this.shortCode = shortCode;
        this.originalUrl = originalUrl;
        this.urlType = urlType;
        this.createdBy = createdBy;
    }
}