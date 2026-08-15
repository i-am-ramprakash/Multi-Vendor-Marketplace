package com.marketplace.product.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Image response")
public class ImageResponse {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "https://example.com/image.jpg")
    private String url;

    @Schema(example = "Classic T-Shirt - Red")
    private String altText;

    @Schema(example = "0")
    private Integer position;

    @Schema(example = "true")
    private Boolean isPrimary;

    @Schema(example = "1")
    private Long variantId;

    @Schema(example = "2024-01-15T10:30:00Z")
    private Instant createdAt;

    public static ImageResponse from(com.marketplace.product.domain.entity.ProductImage image) {
        return ImageResponse.builder()
            .id(image.getId())
            .url(image.getUrl())
            .altText(image.getAltText())
            .position(image.getPosition())
            .isPrimary(image.getIsPrimary())
            .variantId(image.getVariant() != null ? image.getVariant().getId() : null)
            .createdAt(image.getCreatedAt())
            .build();
    }
}