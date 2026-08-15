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

import java.util.List;

@RestController
@RequestMapping("/v1/admin/notifications")
@RequiredArgsConstructor
@Tag(name = "Admin Notification Management", description = "Admin notification management endpoints")
@PreAuthorize("hasRole('ADMIN')")
public class AdminNotificationController {

    private final NotificationService notificationService;

    @PostMapping("/templates")
    @Operation(summary = "Create notification template", description = "Creates a new notification template")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Template created successfully", content = @Content(schema = @Schema(implementation = TemplateResponse.class))),
        @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(implementation = com.marketplace.notification.api.advice.ErrorResponse.class)))
    })
    public ResponseEntity<TemplateResponse> createTemplate(@Valid @RequestBody CreateTemplateRequest request) {
        TemplateResponse response = notificationService.createTemplate(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/templates")
    @Operation(summary = "Get all templates", description = "Returns all active notification templates")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Templates retrieved successfully", content = @Content(schema = @Schema(implementation = TemplateResponse.class)))
    })
    public ResponseEntity<List<TemplateResponse>> getAllTemplates() {
        List<TemplateResponse> response = notificationService.getAllTemplates();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/templates/{id}")
    @Operation(summary = "Get template by ID", description = "Returns notification template by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Template retrieved successfully", content = @Content(schema = @Schema(implementation = TemplateResponse.class))),
        @ApiResponse(responseCode = "404", description = "Template not found", content = @Content(schema = @Schema(implementation = com.marketplace.notification.api.advice.ErrorResponse.class)))
    })
    public ResponseEntity<TemplateResponse> getTemplate(@PathVariable Long id) {
        TemplateResponse response = notificationService.getTemplate(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/templates/code/{code}")
    @Operation(summary = "Get template by code", description = "Returns notification template by code")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Template retrieved successfully", content = @Content(schema = @Schema(implementation = TemplateResponse.class))),
        @ApiResponse(responseCode = "404", description = "Template not found", content = @Content(schema = @Schema(implementation = com.marketplace.notification.api.advice.ErrorResponse.class)))
    })
    public ResponseEntity<TemplateResponse> getTemplateByCode(@PathVariable String code) {
        TemplateResponse response = notificationService.getTemplateByCode(code);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/dead-letter")
    @Operation(summary = "Get unresolved dead letter messages", description = "Returns all unresolved dead letter messages")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Dead letter messages retrieved successfully", content = @Content(schema = @Schema(implementation = DeadLetterResponse.class)))
    })
    public ResponseEntity<List<DeadLetterResponse>> getUnresolvedDeadLetterMessages() {
        List<DeadLetterResponse> response = notificationService.getUnresolvedDeadLetterMessages();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/dead-letter/{id}/resolve")
    @Operation(summary = "Resolve dead letter message", description = "Resolves a dead letter message")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Dead letter message resolved successfully", content = @Content(schema = @Schema(implementation = DeadLetterResponse.class))),
        @ApiResponse(responseCode = "404", description = "Dead letter message not found", content = @Content(schema = @Schema(implementation = com.marketplace.notification.api.advice.ErrorResponse.class)))
    })
    public ResponseEntity<DeadLetterResponse> resolveDeadLetterMessage(
            @PathVariable Long id,
            @RequestParam Long resolvedBy,
            @RequestParam String resolutionNotes) {
        DeadLetterResponse response = notificationService.resolveDeadLetterMessage(id, resolvedBy, resolutionNotes);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/dead-letter/{id}")
    @Operation(summary = "Get dead letter message", description = "Returns dead letter message by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Dead letter message retrieved successfully", content = @Content(schema = @Schema(implementation = DeadLetterResponse.class))),
        @ApiResponse(responseCode = "404", description = "Dead letter message not found", content = @Content(schema = @Schema(implementation = com.marketplace.notification.api.advice.ErrorResponse.class)))
    })
    public ResponseEntity<DeadLetterResponse> getDeadLetterMessage(@PathVariable Long id) {
        DeadLetterResponse response = notificationService.getDeadLetterMessage(id);
        return ResponseEntity.ok(response);
    }
}