package com.marketplace.urlshortener.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateShortUrlRequest {

    @NotBlank(message = "Original URL is required")
    private String originalUrl;

    private String title;

    private String description;

    @NotNull(message = "URL type is required")
    private String type;

    private String customAlias;

    private String expirationType;

    private Long expirationMinutes;

    private Long referenceId;

    private String referenceType;

    private Long createdBy;

    private String password;

    private String tags;

    private String metadata;
}