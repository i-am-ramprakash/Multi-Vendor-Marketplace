package com.marketplace.notification.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TemplateResponse {

    private Long id;
    private String code;
    private String name;
    private String description;
    private String type;
    private String channel;
    private String subjectTemplate;
    private String bodyTemplate;
    private String htmlTemplate;
    private boolean isActive;
    private String locale;
    private String version;
    private Instant createdAt;
    private Instant updatedAt;

    public static TemplateResponse from(com.marketplace.notification.domain.entity.NotificationTemplate template) {
        return TemplateResponse.builder()
            .id(template.getId())
            .code(template.getCode())
            .name(template.getName())
            .description(template.getDescription())
            .type(template.getType().name())
            .channel(template.getChannel().name())
            .subjectTemplate(template.getSubjectTemplate())
            .bodyTemplate(template.getBodyTemplate())
            .htmlTemplate(template.getHtmlTemplate())
            .isActive(template.isActive())
            .locale(template.getLocale())
            .version(template.getVersion())
            .createdAt(template.getCreatedAt())
            .updatedAt(template.getUpdatedAt())
            .build();
    }
}