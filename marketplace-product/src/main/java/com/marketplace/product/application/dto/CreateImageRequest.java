package com.marketplace.product.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Create image request")
public class CreateImageRequest {

    @NotBlank(message = "Image URL is required")
    @Size(max = 500, message = "Image URL cannot exceed 500 characters")
    @Schema(example = "https://example.com/image.jpg", requiredMode = Schema.RequiredMode.REQUIRED)
    private String url;

    @Size(max = 255, message = "Alt text cannot exceed 255 characters")
    @Schema(example = "Classic T-Shirt - Red")
    private String altText;

    @Schema(example = "0")
    private Integer position;

    @Schema(example = "true")
    private Boolean isPrimary;

    @Schema(example = "1")
    private Long variantId;
}