package com.marketplace.auth.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Generic message response")
public class MessageResponse {

    @Schema(example = "Operation completed successfully")
    private String message;

    @Schema(example = "true")
    private boolean success;

    public static MessageResponse success(String message) {
        return MessageResponse.builder().message(message).success(true).build();
    }

    public static MessageResponse error(String message) {
        return MessageResponse.builder().message(message).success(false).build();
    }
}