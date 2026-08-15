package com.marketplace.admin.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Export request")
public class ExportRequest {

    @Schema(description = "Report type to export", example = "TOP_VENDORS", required = true)
    private String reportType;

    @Schema(description = "Export format", example = "CSV", required = true)
    private String format;

    @Schema(description = "Start date for date range filter")
    private String fromDate;

    @Schema(description = "End date for date range filter")
    private String toDate;

    @Schema(description = "Filter by category ID")
    private Long categoryId;

    @Schema(description = "Filter by vendor ID")
    private Long vendorId;
}