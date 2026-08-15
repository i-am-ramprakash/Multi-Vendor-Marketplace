package com.marketplace.notification.application.usecase;

import com.marketplace.notification.application.dto.CreateNotificationRequest;
import com.marketplace.notification.application.dto.NotificationResponse;
import com.marketplace.notification.domain.entity.Notification;
import com.marketplace.notification.domain.entity.NotificationAuditLog;
import com.marketplace.notification.domain.entity.NotificationTemplate;
import com.marketplace.notification.domain.event.NotificationSentEvent;
import com.marketplace.notification.domain.exception.NotificationSendException;
import com.marketplace.notification.domain.exception.TemplateNotFoundException;
import com.marketplace.notification.domain.repository.NotificationAuditLogRepository;
import com.marketplace.notification.domain.repository.NotificationRepository;
import com.marketplace.notification.domain.repository.NotificationTemplateRepository;
import com.marketplace.notification.domain.service.EmailService;
import com.marketplace.notification.domain.service.KafkaProducerService;
import com.marketplace.notification.domain.valueobject.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SendNotificationUseCase {

    private final NotificationRepository notificationRepository;
    private final NotificationTemplateRepository templateRepository;
    private final NotificationAuditLogRepository auditLogRepository;
    private final KafkaProducerService kafkaProducerService;
    private final EmailService emailService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public NotificationResponse execute(CreateNotificationRequest request) {
        NotificationType type = NotificationType.valueOf(request.getType());
        NotificationChannel channel = NotificationChannel.valueOf(request.getChannel());
        NotificationPriority priority = NotificationPriority.valueOf(
            request.getPriority() != null ? request.getPriority() : "NORMAL"
        );

        // Find and render template if templateCode is provided
        String subject = request.getSubject();
        String body = request.getBody();

        if (request.getTemplateCode() != null && !request.getTemplateCode().isBlank()) {
            NotificationTemplate template = templateRepository.findByCode(request.getTemplateCode())
                .orElseThrow(() -> new TemplateNotFoundException(request.getTemplateCode()));

            if (request.getTemplateVariables() != null) {
                subject = renderTemplate(template.getSubjectTemplate(), request.getTemplateVariables());
                body = renderTemplate(template.getBodyTemplate(), request.getTemplateVariables());
            } else {
                subject = template.getSubjectTemplate();
                body = template.getBodyTemplate();
            }
        }

        // Create notification entity
        Notification notification = new Notification(
            request.getReferenceId(),
            type,
            channel,
            request.getRecipientId(),
            request.getRecipientEmail(),
            subject,
            body
        );
        notification.setPriority(priority);
        notification.setRecipientPhone(request.getRecipientPhone());
        notification.setTemplateCode(request.getTemplateCode());
        notification.setTemplateVariables(request.getTemplateVariables());
        notification.setMetadata(request.getMetadata());

        // Queue notification
        notification.queue();

        // Save notification
        Notification savedNotification = notificationRepository.save(notification);

        // Create audit log
        NotificationAuditLog auditLog = new NotificationAuditLog(
            savedNotification.getId(),
            "NOTIFICATION_QUEUED",
            "Notification queued for processing",
            null
        );
        auditLogRepository.save(auditLog);

        // Send to Kafka for async processing
        try {
            kafkaProducerService.sendNotification(savedNotification);
            savedNotification.process();
            notificationRepository.save(savedNotification);
        } catch (Exception e) {
            savedNotification.markFailed("Failed to send to Kafka: " + e.getMessage());
            notificationRepository.save(savedNotification);
            throw new NotificationSendException("Failed to queue notification", e);
        }

        return NotificationResponse.from(savedNotification);
    }

    private String renderTemplate(String template, Map<String, String> variables) {
        if (template == null || variables == null) return template;

        String rendered = template;
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            rendered = rendered.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
        return rendered;
    }
}