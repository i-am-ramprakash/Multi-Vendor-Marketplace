package com.marketplace.notification.application.service;

import com.marketplace.notification.application.dto.*;
import com.marketplace.notification.application.usecase.*;
import com.marketplace.notification.domain.entity.NotificationTemplate;
import com.marketplace.notification.domain.repository.DeadLetterMessageRepository;
import com.marketplace.notification.domain.repository.NotificationTemplateRepository;
import com.marketplace.notification.domain.valueobject.NotificationChannel;
import com.marketplace.notification.domain.valueobject.NotificationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private SendNotificationUseCase sendNotificationUseCase;

    @Mock
    private GetNotificationUseCase getNotificationUseCase;

    @Mock
    private GetUserNotificationsUseCase getUserNotificationsUseCase;

    @Mock
    private RetryNotificationUseCase retryNotificationUseCase;

    @Mock
    private CancelNotificationUseCase cancelNotificationUseCase;

    @Mock
    private GetNotificationStatsUseCase getNotificationStatsUseCase;

    @Mock
    private NotificationTemplateRepository templateRepository;

    @Mock
    private DeadLetterMessageRepository deadLetterMessageRepository;

    private NotificationServiceImpl notificationService;

    @BeforeEach
    void setUp() {
        notificationService = new NotificationServiceImpl(
            sendNotificationUseCase,
            getNotificationUseCase,
            getUserNotificationsUseCase,
            retryNotificationUseCase,
            cancelNotificationUseCase,
            getNotificationStatsUseCase,
            templateRepository,
            deadLetterMessageRepository
        );
    }

    @Test
    void sendNotification_WithValidRequest_ShouldDelegateToUseCase() {
        // Given
        CreateNotificationRequest request = CreateNotificationRequest.builder()
            .type("ORDER_CREATED")
            .channel("EMAIL")
            .recipientId(1L)
            .recipientEmail("test@example.com")
            .subject("Test Subject")
            .body("Test Body")
            .build();

        NotificationResponse expectedResponse = NotificationResponse.builder()
            .id(1L)
            .type("ORDER_CREATED")
            .channel("EMAIL")
            .status("QUEUED")
            .build();

        when(sendNotificationUseCase.execute(request)).thenReturn(expectedResponse);

        // When
        NotificationResponse response = notificationService.sendNotification(request);

        // Then
        assertThat(response).isEqualTo(expectedResponse);
        verify(sendNotificationUseCase).execute(request);
    }

    @Test
    void getNotification_WithValidId_ShouldDelegateToUseCase() {
        // Given
        NotificationResponse expectedResponse = NotificationResponse.builder()
            .id(1L)
            .type("ORDER_CREATED")
            .channel("EMAIL")
            .status("SENT")
            .build();

        when(getNotificationUseCase.execute(1L)).thenReturn(expectedResponse);

        // When
        NotificationResponse response = notificationService.getNotification(1L);

        // Then
        assertThat(response).isEqualTo(expectedResponse);
        verify(getNotificationUseCase).execute(1L);
    }

    @Test
    void getNotificationByReferenceId_WithValidReferenceId_ShouldDelegateToUseCase() {
        // Given
        NotificationResponse expectedResponse = NotificationResponse.builder()
            .id(1L)
            .referenceId("ref-123")
            .type("ORDER_CREATED")
            .channel("EMAIL")
            .status("SENT")
            .build();

        when(getNotificationUseCase.executeByReferenceId("ref-123")).thenReturn(expectedResponse);

        // When
        NotificationResponse response = notificationService.getNotificationByReferenceId("ref-123");

        // Then
        assertThat(response).isEqualTo(expectedResponse);
        verify(getNotificationUseCase).executeByReferenceId("ref-123");
    }

    @Test
    void getUserNotifications_WithValidUser_ShouldDelegateToUseCase() {
        // Given
        NotificationListResponse expectedResponse = NotificationListResponse.builder()
            .totalElements(1)
            .build();

        when(getUserNotificationsUseCase.execute(1L, 0, 10)).thenReturn(expectedResponse);

        // When
        NotificationListResponse response = notificationService.getUserNotifications(1L, 0, 10);

        // Then
        assertThat(response).isEqualTo(expectedResponse);
        verify(getUserNotificationsUseCase).execute(1L, 0, 10);
    }

    @Test
    void retryNotification_WithValidId_ShouldDelegateToUseCase() {
        // Given
        NotificationResponse expectedResponse = NotificationResponse.builder()
            .id(1L)
            .status("RETRYING")
            .build();

        when(retryNotificationUseCase.execute(1L)).thenReturn(expectedResponse);

        // When
        NotificationResponse response = notificationService.retryNotification(1L);

        // Then
        assertThat(response).isEqualTo(expectedResponse);
        verify(retryNotificationUseCase).execute(1L);
    }

    @Test
    void cancelNotification_WithValidId_ShouldDelegateToUseCase() {
        // Given
        NotificationResponse expectedResponse = NotificationResponse.builder()
            .id(1L)
            .status("CANCELLED")
            .build();

        when(cancelNotificationUseCase.execute(1L, 10L)).thenReturn(expectedResponse);

        // When
        NotificationResponse response = notificationService.cancelNotification(1L, 10L);

        // Then
        assertThat(response).isEqualTo(expectedResponse);
        verify(cancelNotificationUseCase).execute(1L, 10L);
    }

    @Test
    void getNotificationStats_ShouldDelegateToUseCase() {
        // Given
        NotificationStatsResponse expectedResponse = NotificationStatsResponse.builder()
            .totalNotifications(100)
            .sentCount(80)
            .deliveredCount(70)
            .failedCount(10)
            .build();

        when(getNotificationStatsUseCase.execute()).thenReturn(expectedResponse);

        // When
        NotificationStatsResponse response = notificationService.getNotificationStats();

        // Then
        assertThat(response).isEqualTo(expectedResponse);
        verify(getNotificationStatsUseCase).execute();
    }

    @Test
    void createTemplate_WithValidRequest_ShouldSaveTemplate() {
        // Given
        CreateTemplateRequest request = CreateTemplateRequest.builder()
            .code("ORDER_CREATED")
            .name("Order Created Template")
            .type("ORDER_CREATED")
            .channel("EMAIL")
            .subjectTemplate("Order Confirmation")
            .bodyTemplate("Your order has been placed.")
            .build();

        NotificationTemplate savedTemplate = new NotificationTemplate(
            "ORDER_CREATED",
            "Order Created Template",
            NotificationType.ORDER_CREATED,
            NotificationChannel.EMAIL,
            "Order Confirmation",
            "Your order has been placed."
        );

        when(templateRepository.save(any())).thenReturn(savedTemplate);

        // When
        TemplateResponse response = notificationService.createTemplate(request);

        // Then
        assertThat(response.getCode()).isEqualTo("ORDER_CREATED");
        assertThat(response.getName()).isEqualTo("Order Created Template");
        verify(templateRepository).save(any());
    }

    @Test
    void getAllTemplates_ShouldReturnActiveTemplates() {
        // Given
        List<NotificationTemplate> templates = List.of(
            new NotificationTemplate("ORDER_CREATED", "Order Created", NotificationType.ORDER_CREATED, NotificationChannel.EMAIL, "Subject", "Body"),
            new NotificationTemplate("ORDER_SHIPPED", "Order Shipped", NotificationType.ORDER_SHIPPED, NotificationChannel.EMAIL, "Subject", "Body")
        );

        when(templateRepository.findByIsActiveTrue()).thenReturn(templates);

        // When
        List<TemplateResponse> response = notificationService.getAllTemplates();

        // Then
        assertThat(response).hasSize(2);
        verify(templateRepository).findByIsActiveTrue();
    }
}