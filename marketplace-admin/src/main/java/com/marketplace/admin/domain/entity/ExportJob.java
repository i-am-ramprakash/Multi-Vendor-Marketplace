package com.marketplace.admin.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExportJob {
    private Long id;
    private String jobId;
    private String reportType;
    private String format;
    private String status;
    private String filePath;
    private Long totalRecords;
    private Long processedRecords;
    private String errorMessage;
    private String requestedBy;
}