package com.marketplace.auth.api.advice;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Error response")
public class ErrorResponse {

    @Schema(example = "2024-01-15T10:30:00Z")
    private Instant timestamp;

    @Schema(example = "400")
    private int status;

    @Schema(example = "Bad Request")
    private String error;

    @Schema(example = "VALIDATION_ERROR")
    private String code;

    @Schema(example = "Validation failed")
    private String message;

    @Schema(example = "/v1/auth/login")
    private String path;

    @Schema(description = "Validation errors for each field")
    private Map<String, String> errors;
}