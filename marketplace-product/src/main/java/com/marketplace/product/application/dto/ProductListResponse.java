package com.marketplace.product.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Product list response with pagination")
public class ProductListResponse {

    @Schema(description = "List of products")
    private List<ProductResponse> products;

    @Schema(description = "Total number of products")
    private Long totalElements;

    @Schema(description = "Total number of pages")
    private Integer totalPages;

    @Schema(description = "Current page number")
    private Integer currentPage;

    @Schema(description = "Page size")
    private Integer pageSize;

    @Schema(description = "Whether there is a next page")
    private Boolean hasNext;

    @Schema(description = "Whether there is a previous page")
    private Boolean hasPrevious;

    @Schema(description = "Sort field")
    private String sortBy;

    @Schema(description = "Sort direction")
    private String sortDirection;
}