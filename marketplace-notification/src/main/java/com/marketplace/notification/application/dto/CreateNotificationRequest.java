package com.marketplace.notification.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateNotificationRequest {

    private String referenceId;

    @NotBlank(message = "Notification type is required")
    private String type;

    @NotBlank(message = "Channel is required")
    private String channel;

    private String priority;

    @NotNull(message = "Recipient ID is required")
    private Long recipientId;

    @NotBlank(message = "Recipient email is required")
    @Email(message = "Invalid email format")
    private String recipientEmail;

    private String recipientPhone;

    private String subject;

    private String body;

    private String templateCode;

    private Map<String, String> templateVariables;

    private String metadata;
}