package com.marketplace.notification.domain.event;

import lombok.Getter;

@Getter
public class NotificationSentEvent extends DomainEvent {

    private final Long notificationId;
    private final String referenceId;
    private final String recipientEmail;
    private final String channel;

    public NotificationSentEvent(Object source, Long notificationId, String referenceId,
                                String recipientEmail, String channel) {
        super(source);
        this.notificationId = notificationId;
        this.referenceId = referenceId;
        this.recipientEmail = recipientEmail;
        this.channel = channel;
    }
}