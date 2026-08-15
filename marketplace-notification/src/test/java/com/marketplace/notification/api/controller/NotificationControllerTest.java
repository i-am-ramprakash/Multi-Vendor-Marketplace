package com.marketplace.notification.api.controller;

import com.marketplace.notification.application.dto.*;
import com.marketplace.notification.application.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void sendNotification_WithValidRequest_ShouldReturn200() throws Exception {
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

        when(notificationService.sendNotification(any(CreateNotificationRequest.class)))
                .thenReturn(expectedResponse);

        // When & Then
        mockMvc.perform(post("/v1/notifications")
                .contentType("application/json")
                .content(
                        "{\"type\":\"ORDER_CREATED\",\"channel\":\"EMAIL\",\"recipientId\":1,\"recipientEmail\":\"test@example.com\",\"subject\":\"Test Subject\",\"body\":\"Test Body\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.type").value("ORDER_CREATED"))
                .andExpect(jsonPath("$.channel").value("EMAIL"))
                .andExpect(jsonPath("$.status").value("QUEUED"));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void getNotification_WithValidId_ShouldReturn200() throws Exception {
        // Given
        NotificationResponse expectedResponse = NotificationResponse.builder()
                .id(1L)
                .type("ORDER_CREATED")
                .channel("EMAIL")
                .status("SENT")
                .build();

        when(notificationService.getNotification(1L)).thenReturn(expectedResponse);

        // When & Then
        mockMvc.perform(get("/v1/notifications/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.type").value("ORDER_CREATED"))
                .andExpect(jsonPath("$.status").value("SENT"));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void getNotificationByReferenceId_WithValidReferenceId_ShouldReturn200() throws Exception {
        // Given
        NotificationResponse expectedResponse = NotificationResponse.builder()
                .id(1L)
                .referenceId("ref-123")
                .type("ORDER_CREATED")
                .channel("EMAIL")
                .status("SENT")
                .build();

        when(notificationService.getNotificationByReferenceId("ref-123")).thenReturn(expectedResponse);

        // When & Then
        mockMvc.perform(get("/v1/notifications/reference/ref-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.referenceId").value("ref-123"));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void getUserNotifications_WithValidUser_ShouldReturn200() throws Exception {
        // Given
        NotificationListResponse expectedResponse = NotificationListResponse.builder()
                .totalElements(1)
                .build();

        when(notificationService.getUserNotifications(1L, 0, 10)).thenReturn(expectedResponse);

        // When & Then
        mockMvc.perform(get("/v1/notifications/user/1")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void retryNotification_WithValidId_ShouldReturn200() throws Exception {
        // Given
        NotificationResponse expectedResponse = NotificationResponse.builder()
                .id(1L)
                .status("RETRYING")
                .build();

        when(notificationService.retryNotification(1L)).thenReturn(expectedResponse);

        // When & Then
        mockMvc.perform(put("/v1/notifications/1/retry"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("RETRYING"));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void cancelNotification_WithValidId_ShouldReturn200() throws Exception {
        // Given
        NotificationResponse expectedResponse = NotificationResponse.builder()
                .id(1L)
                .status("CANCELLED")
                .build();

        when(notificationService.cancelNotification(1L, 10L)).thenReturn(expectedResponse);

        // When & Then
        mockMvc.perform(put("/v1/notifications/1/cancel")
                .param("performedBy", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getNotificationStats_ShouldReturn200() throws Exception {
        // Given
        NotificationStatsResponse expectedResponse = NotificationStatsResponse.builder()
                .totalNotifications(100)
                .sentCount(80)
                .deliveredCount(70)
                .failedCount(10)
                .build();

        when(notificationService.getNotificationStats()).thenReturn(expectedResponse);

        // When & Then
        mockMvc.perform(get("/v1/notifications/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalNotifications").value(100))
                .andExpect(jsonPath("$.sentCount").value(80))
                .andExpect(jsonPath("$.deliveredCount").value(70))
                .andExpect(jsonPath("$.failedCount").value(10));
    }
}