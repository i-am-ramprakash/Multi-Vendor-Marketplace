package com.marketplace.notification.domain.event;

import lombok.Getter;

@Getter
public class NotificationFailedEvent extends DomainEvent {

    private final Long notificationId;
    private final String referenceId;
    private final String recipientEmail;
    private final String failureReason;
    private final int retryCount;

    public NotificationFailedEvent(Object source, Long notificationId, String referenceId,
                                  String recipientEmail, String failureReason, int retryCount) {
        super(source);
        this.notificationId = notificationId;
        this.referenceId = referenceId;
        this.recipientEmail = recipientEmail;
        this.failureReason = failureReason;
        this.retryCount = retryCount;
    }
}