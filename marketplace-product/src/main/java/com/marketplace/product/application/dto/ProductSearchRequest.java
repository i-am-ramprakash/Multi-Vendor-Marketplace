package com.marketplace.product.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Product search request")
public class ProductSearchRequest {

    @Schema(example = "t-shirt", description = "Search keyword")
    private String keyword;

    @Schema(example = "1", description = "Category ID")
    private Long categoryId;

    @Schema(example = "1", description = "Vendor ID")
    private Long vendorId;

    @Schema(example = "APPROVED", description = "Product status")
    private String status;

    @Schema(example = "10.00", description = "Minimum price")
    private BigDecimal minPrice;

    @Schema(example = "100.00", description = "Maximum price")
    private BigDecimal maxPrice;

    @Schema(example = "true", description = "In stock only")
    private Boolean inStockOnly;

    @Schema(example = "true", description = "Featured only")
    private Boolean featuredOnly;

    @Schema(example = "0", description = "Page number")
    private Integer page;

    @Schema(example = "10", description = "Page size")
    private Integer size;

    @Schema(example = "createdAt", description = "Sort field")
    private String sortBy;

    @Schema(example = "desc", description = "Sort direction")
    private String sortDirection;

    public int getPage() {
        return page != null && page >= 0 ? page : 0;
    }

    public int getSize() {
        return size != null && size > 0 && size <= 100 ? size : 10;
    }

    public String getSortBy() {
        return sortBy != null ? sortBy : "createdAt";
    }

    public String getSortDirection() {
        return sortDirection != null ? sortDirection.toUpperCase() : "DESC";
    }
}