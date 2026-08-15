package com.marketplace.vendor.application.dto;

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
@Schema(description = "Vendor list response with pagination")
public class VendorListResponse {

    @Schema(description = "List of vendors")
    private List<VendorResponse> vendors;

    @Schema(description = "Total number of vendors")
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
}