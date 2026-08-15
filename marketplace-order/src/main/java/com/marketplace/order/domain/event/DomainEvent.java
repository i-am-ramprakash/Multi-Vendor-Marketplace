package com.marketplace.order.domain.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.Instant;

@Getter
public abstract class DomainEvent extends ApplicationEvent {

    private final Instant occurredOn;
    private final String eventId;

    protected DomainEvent(Object source) {
        super(source);
        this.occurredOn = Instant.now();
        this.eventId = java.util.UUID.randomUUID().toString();
    }
}