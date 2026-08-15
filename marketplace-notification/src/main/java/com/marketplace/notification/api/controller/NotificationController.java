package com.marketplace.notification.api.controller;

import com.marketplace.notification.application.dto.*;
import com.marketplace.notification.application.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification Management", description = "Notification management endpoints")
@PreAuthorize("hasAnyRole('CUSTOMER', 'VENDOR', 'ADMIN')")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    @Operation(summary = "Send notification", description = "Sends a new notification")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notification sent successfully", content = @Content(schema = @Schema(implementation = NotificationResponse.class))),
        @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(implementation = com.marketplace.notification.api.advice.ErrorResponse.class)))
    })
    public ResponseEntity<NotificationResponse> sendNotification(@Valid @RequestBody CreateNotificationRequest request) {
        NotificationResponse response = notificationService.sendNotification(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get notification", description = "Returns notification details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notification retrieved successfully", content = @Content(schema = @Schema(implementation = NotificationResponse.class))),
        @ApiResponse(responseCode = "404", description = "Notification not found", content = @Content(schema = @Schema(implementation = com.marketplace.notification.api.advice.ErrorResponse.class)))
    })
    public ResponseEntity<NotificationResponse> getNotification(@PathVariable Long id) {
        NotificationResponse response = notificationService.getNotification(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/reference/{referenceId}")
    @Operation(summary = "Get notification by reference", description = "Returns notification by reference ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notification retrieved successfully", content = @Content(schema = @Schema(implementation = NotificationResponse.class))),
        @ApiResponse(responseCode = "404", description = "Notification not found", content = @Content(schema = @Schema(implementation = com.marketplace.notification.api.advice.ErrorResponse.class)))
    })
    public ResponseEntity<NotificationResponse> getNotificationByReferenceId(@PathVariable String referenceId) {
        NotificationResponse response = notificationService.getNotificationByReferenceId(referenceId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get user notifications", description = "Returns paginated list of user notifications")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notifications retrieved successfully", content = @Content(schema = @Schema(implementation = NotificationListResponse.class)))
    })
    public ResponseEntity<NotificationListResponse> getUserNotifications(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        NotificationListResponse response = notificationService.getUserNotifications(userId, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}/status/{status}")
    @Operation(summary = "Get user notifications by status", description = "Returns paginated list of user notifications with specific status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notifications retrieved successfully", content = @Content(schema = @Schema(implementation = NotificationListResponse.class)))
    })
    public ResponseEntity<NotificationListResponse> getUserNotificationsByStatus(
            @PathVariable Long userId,
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        NotificationListResponse response = notificationService.getUserNotificationsByStatus(userId, status, page, size);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/retry")
    @Operation(summary = "Retry notification", description = "Retries a failed notification")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notification retried successfully", content = @Content(schema = @Schema(implementation = NotificationResponse.class))),
        @ApiResponse(responseCode = "404", description = "Notification not found", content = @Content(schema = @Schema(implementation = com.marketplace.notification.api.advice.ErrorResponse.class))),
        @ApiResponse(responseCode = "400", description = "Notification cannot be retried", content = @Content(schema = @Schema(implementation = com.marketplace.notification.api.advice.ErrorResponse.class)))
    })
    public ResponseEntity<NotificationResponse> retryNotification(@PathVariable Long id) {
        NotificationResponse response = notificationService.retryNotification(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "Cancel notification", description = "Cancels a notification")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notification cancelled successfully", content = @Content(schema = @Schema(implementation = NotificationResponse.class))),
        @ApiResponse(responseCode = "404", description = "Notification not found", content = @Content(schema = @Schema(implementation = com.marketplace.notification.api.advice.ErrorResponse.class))),
        @ApiResponse(responseCode = "400", description = "Notification cannot be cancelled", content = @Content(schema = @Schema(implementation = com.marketplace.notification.api.advice.ErrorResponse.class)))
    })
    public ResponseEntity<NotificationResponse> cancelNotification(
            @PathVariable Long id,
            @RequestParam Long performedBy) {
        NotificationResponse response = notificationService.cancelNotification(id, performedBy);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stats")
    @Operation(summary = "Get notification stats", description = "Returns notification statistics")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Stats retrieved successfully", content = @Content(schema = @Schema(implementation = NotificationStatsResponse.class)))
    })
    public ResponseEntity<NotificationStatsResponse> getNotificationStats() {
        NotificationStatsResponse response = notificationService.getNotificationStats();
        return ResponseEntity.ok(response);
    }
}