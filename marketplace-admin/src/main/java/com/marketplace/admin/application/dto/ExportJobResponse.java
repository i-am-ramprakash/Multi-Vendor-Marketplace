package com.marketplace.admin.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Export job response")
public class ExportJobResponse {

    @Schema(description = "Export job ID")
    private String jobId;

    @Schema(description = "Report type")
    private String reportType;

    @Schema(description = "Export format")
    private String format;

    @Schema(description = "Job status", example = "COMPLETED")
    private String status;

    @Schema(description = "Download URL when completed")
    private String downloadUrl;

    @Schema(description = "Total records to export")
    private Long totalRecords;

    @Schema(description = "Records processed so far")
    private Long processedRecords;

    @Schema(description = "Error message if failed")
    private String errorMessage;

    @Schema(description = "Job creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Job completion timestamp")
    private LocalDateTime completedAt;
}