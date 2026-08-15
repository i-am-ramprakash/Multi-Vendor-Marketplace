package com.marketplace.admin.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Filter criteria for dashboard queries")
public class DashboardFilterRequest {

    @Schema(description = "Start date for date range filter", example = "2024-01-01")
    private LocalDate fromDate;

    @Schema(description = "End date for date range filter", example = "2024-12-31")
    private LocalDate toDate;

    @Schema(description = "Filter by vendor ID")
    private Long vendorId;

    @Schema(description = "Filter by category ID")
    private Long categoryId;

    @Schema(description = "Filter by order status", example = "COMPLETED")
    private String orderStatus;

    @Schema(description = "Filter by vendor status", example = "APPROVED")
    private String vendorStatus;

    @Schema(description = "Filter by product status", example = "APPROVED")
    private String productStatus;

    @Schema(description = "Page number (0-based)", example = "0")
    @Min(0)
    private Integer page;

    @Schema(description = "Page size", example = "20")
    @Min(1)
    @Max(100)
    private Integer size;

    @Schema(description = "Sort field", example = "revenue")
    private String sortBy;

    @Schema(description = "Sort direction", example = "desc")
    private String sortDirection;
}