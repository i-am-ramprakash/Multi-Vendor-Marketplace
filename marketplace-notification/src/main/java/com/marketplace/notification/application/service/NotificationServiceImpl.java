package com.marketplace.notification.application.service;

import com.marketplace.notification.application.dto.*;
import com.marketplace.notification.application.usecase.*;
import com.marketplace.notification.domain.entity.DeadLetterMessage;
import com.marketplace.notification.domain.entity.NotificationTemplate;
import com.marketplace.notification.domain.exception.DeadLetterNotFoundException;
import com.marketplace.notification.domain.exception.TemplateNotFoundException;
import com.marketplace.notification.domain.repository.DeadLetterMessageRepository;
import com.marketplace.notification.domain.repository.NotificationTemplateRepository;
import com.marketplace.notification.domain.valueobject.NotificationChannel;
import com.marketplace.notification.domain.valueobject.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final SendNotificationUseCase sendNotificationUseCase;
    private final GetNotificationUseCase getNotificationUseCase;
    private final GetUserNotificationsUseCase getUserNotificationsUseCase;
    private final RetryNotificationUseCase retryNotificationUseCase;
    private final CancelNotificationUseCase cancelNotificationUseCase;
    private final GetNotificationStatsUseCase getNotificationStatsUseCase;
    private final NotificationTemplateRepository templateRepository;
    private final DeadLetterMessageRepository deadLetterMessageRepository;

    @Override
    @Transactional
    public NotificationResponse sendNotification(CreateNotificationRequest request) {
        return sendNotificationUseCase.execute(request);
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationResponse getNotification(Long id) {
        return getNotificationUseCase.execute(id);
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationResponse getNotificationByReferenceId(String referenceId) {
        return getNotificationUseCase.executeByReferenceId(referenceId);
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationListResponse getUserNotifications(Long userId, int page, int size) {
        return getUserNotificationsUseCase.execute(userId, page, size);
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationListResponse getUserNotificationsByStatus(Long userId, String status, int page, int size) {
        return getUserNotificationsUseCase.execute(userId, status, page, size);
    }

    @Override
    @Transactional
    public NotificationResponse retryNotification(Long notificationId) {
        return retryNotificationUseCase.execute(notificationId);
    }

    @Override
    @Transactional
    public NotificationResponse cancelNotification(Long notificationId, Long performedBy) {
        return cancelNotificationUseCase.execute(notificationId, performedBy);
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationStatsResponse getNotificationStats() {
        return getNotificationStatsUseCase.execute();
    }

    @Override
    @Transactional
    public TemplateResponse createTemplate(CreateTemplateRequest request) {
        NotificationType type = NotificationType.valueOf(request.getType());
        NotificationChannel channel = NotificationChannel.valueOf(request.getChannel());

        NotificationTemplate template = new NotificationTemplate(
            request.getCode(),
            request.getName(),
            type,
            channel,
            request.getSubjectTemplate(),
            request.getBodyTemplate()
        );
        template.setDescription(request.getDescription());
        template.setHtmlTemplate(request.getHtmlTemplate());
        if (request.getLocale() != null) {
            template.setLocale(request.getLocale());
        }

        NotificationTemplate savedTemplate = templateRepository.save(template);
        return TemplateResponse.from(savedTemplate);
    }

    @Override
    @Transactional(readOnly = true)
    public TemplateResponse getTemplate(Long id) {
        NotificationTemplate template = templateRepository.findById(id)
            .orElseThrow(() -> new TemplateNotFoundException(id));
        return TemplateResponse.from(template);
    }

    @Override
    @Transactional(readOnly = true)
    public TemplateResponse getTemplateByCode(String code) {
        NotificationTemplate template = templateRepository.findByCode(code)
            .orElseThrow(() -> new TemplateNotFoundException(code));
        return TemplateResponse.from(template);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TemplateResponse> getAllTemplates() {
        return templateRepository.findByIsActiveTrue().stream()
            .map(TemplateResponse::from)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DeadLetterResponse getDeadLetterMessage(Long id) {
        DeadLetterMessage deadLetter = deadLetterMessageRepository.findById(id)
            .orElseThrow(() -> new DeadLetterNotFoundException(id));
        return DeadLetterResponse.from(deadLetter);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeadLetterResponse> getUnresolvedDeadLetterMessages() {
        return deadLetterMessageRepository.findByResolved(false).stream()
            .map(DeadLetterResponse::from)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DeadLetterResponse resolveDeadLetterMessage(Long id, Long resolvedBy, String resolutionNotes) {
        DeadLetterMessage deadLetter = deadLetterMessageRepository.findById(id)
            .orElseThrow(() -> new DeadLetterNotFoundException(id));

        deadLetter.resolve(resolvedBy, resolutionNotes);
        DeadLetterMessage savedDeadLetter = deadLetterMessageRepository.save(deadLetter);

        return DeadLetterResponse.from(savedDeadLetter);
    }
}