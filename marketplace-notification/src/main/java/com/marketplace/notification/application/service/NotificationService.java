package com.marketplace.notification.application.service;

import com.marketplace.notification.application.dto.*;

import java.util.List;

public interface NotificationService {

    NotificationResponse sendNotification(CreateNotificationRequest request);

    NotificationResponse getNotification(Long id);

    NotificationResponse getNotificationByReferenceId(String referenceId);

    NotificationListResponse getUserNotifications(Long userId, int page, int size);

    NotificationListResponse getUserNotificationsByStatus(Long userId, String status, int page, int size);

    NotificationResponse retryNotification(Long notificationId);

    NotificationResponse cancelNotification(Long notificationId, Long performedBy);

    NotificationStatsResponse getNotificationStats();

    // Template management
    TemplateResponse createTemplate(CreateTemplateRequest request);

    TemplateResponse getTemplate(Long id);

    TemplateResponse getTemplateByCode(String code);

    List<TemplateResponse> getAllTemplates();

    // Dead letter management
    DeadLetterResponse getDeadLetterMessage(Long id);

    List<DeadLetterResponse> getUnresolvedDeadLetterMessages();

    DeadLetterResponse resolveDeadLetterMessage(Long id, Long resolvedBy, String resolutionNotes);
}