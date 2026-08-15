package com.marketplace.product.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Category response")
public class CategoryResponse {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "Electronics")
    private String name;

    @Schema(example = "electronics")
    private String slug;

    @Schema(example = "Electronic devices and accessories")
    private String description;

    @Schema(example = "https://example.com/category.jpg")
    private String imageUrl;

    @Schema(example = "null")
    private Long parentId;

    @Schema(example = "Parent Category > Electronics")
    private String fullPath;

    @Schema(example = "0")
    private Integer displayOrder;

    @Schema(example = "true")
    private Boolean isActive;

    @Schema(example = "25")
    private Integer productCount;

    @Schema(description = "Subcategories")
    private List<CategoryResponse> children;

    @Schema(example = "2024-01-15T10:30:00Z")
    private Instant createdAt;

    public static CategoryResponse from(com.marketplace.product.domain.entity.Category category) {
        return CategoryResponse.builder()
            .id(category.getId())
            .name(category.getName())
            .slug(category.getSlug())
            .description(category.getDescription())
            .imageUrl(category.getImageUrl())
            .parentId(category.getParent() != null ? category.getParent().getId() : null)
            .fullPath(category.getFullPath())
            .displayOrder(category.getDisplayOrder())
            .isActive(category.getIsActive())
            .productCount(category.getProductCount())
            .createdAt(category.getCreatedAt())
            .build();
    }

    public static CategoryResponse fromWithChildren(com.marketplace.product.domain.entity.Category category) {
        CategoryResponse response = from(category);
        if (category.getChildren() != null && !category.getChildren().isEmpty()) {
            response.setChildren(category.getChildren().stream()
                .map(CategoryResponse::fromWithChildren)
                .toList());
        }
        return response;
    }
}