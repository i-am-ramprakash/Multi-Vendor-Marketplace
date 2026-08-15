package com.marketplace.admin.api.controller;

import com.marketplace.admin.application.dto.*;
import com.marketplace.admin.application.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/v1/admin/dashboard")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Admin Dashboard", description = "Admin dashboard analytics and reporting APIs")
@SecurityRequirement(name = "bearerAuth")
public class AdminDashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/metrics")
    @Operation(summary = "Get dashboard metrics", description = "Get aggregated platform metrics including totals, revenue, and growth rates")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Metrics retrieved successfully",
                    content = @Content(schema = @Schema(implementation = DashboardMetricsResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<DashboardMetricsResponse> getDashboardMetrics(
            @Parameter(description = "Filter criteria") DashboardFilterRequest filter) {
        log.info("GET /v1/admin/dashboard/metrics");
        DashboardMetricsResponse metrics = dashboardService.getDashboardMetrics(filter);
        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/top-vendors")
    @Operation(summary = "Get top vendors", description = "Get top performing vendors ranked by revenue, orders, or rating")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Top vendors retrieved successfully",
                    content = @Content(schema = @Schema(implementation = TopVendorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<TopVendorResponse>> getTopVendors(
            @Parameter(description = "Filter criteria") DashboardFilterRequest filter) {
        log.info("GET /v1/admin/dashboard/top-vendors");
        List<TopVendorResponse> vendors = dashboardService.getTopVendors(filter);
        return ResponseEntity.ok(vendors);
    }

    @GetMapping("/top-products")
    @Operation(summary = "Get top products", description = "Get top selling products ranked by sales, revenue, or rating")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Top products retrieved successfully",
                    content = @Content(schema = @Schema(implementation = TopProductResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<TopProductResponse>> getTopProducts(
            @Parameter(description = "Filter criteria") DashboardFilterRequest filter) {
        log.info("GET /v1/admin/dashboard/top-products");
        List<TopProductResponse> products = dashboardService.getTopProducts(filter);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/revenue")
    @Operation(summary = "Get revenue analytics", description = "Get detailed revenue analytics with daily, weekly, and category breakdowns")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Revenue analytics retrieved successfully",
                    content = @Content(schema = @Schema(implementation = RevenueAnalyticsResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<RevenueAnalyticsResponse> getRevenueAnalytics(
            @Parameter(description = "Filter criteria") DashboardFilterRequest filter) {
        log.info("GET /v1/admin/dashboard/revenue");
        RevenueAnalyticsResponse analytics = dashboardService.getRevenueAnalytics(filter);
        return ResponseEntity.ok(analytics);
    }

    @GetMapping("/users")
    @Operation(summary = "Get user analytics", description = "Get user registration trends and analytics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User analytics retrieved successfully",
                    content = @Content(schema = @Schema(implementation = UserAnalyticsResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<UserAnalyticsResponse> getUserAnalytics(
            @Parameter(description = "Filter criteria") DashboardFilterRequest filter) {
        log.info("GET /v1/admin/dashboard/users");
        UserAnalyticsResponse analytics = dashboardService.getUserAnalytics(filter);
        return ResponseEntity.ok(analytics);
    }

    @GetMapping("/vendors")
    @Operation(summary = "Get vendor analytics", description = "Get vendor performance and analytics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vendor analytics retrieved successfully",
                    content = @Content(schema = @Schema(implementation = VendorAnalyticsResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<VendorAnalyticsResponse> getVendorAnalytics(
            @Parameter(description = "Filter criteria") DashboardFilterRequest filter) {
        log.info("GET /v1/admin/dashboard/vendors");
        VendorAnalyticsResponse analytics = dashboardService.getVendorAnalytics(filter);
        return ResponseEntity.ok(analytics);
    }

    @GetMapping("/products")
    @Operation(summary = "Get product analytics", description = "Get product performance and analytics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product analytics retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ProductAnalyticsResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ProductAnalyticsResponse> getProductAnalytics(
            @Parameter(description = "Filter criteria") DashboardFilterRequest filter) {
        log.info("GET /v1/admin/dashboard/products");
        ProductAnalyticsResponse analytics = dashboardService.getProductAnalytics(filter);
        return ResponseEntity.ok(analytics);
    }

    @GetMapping("/vendors/{vendorId}/performance")
    @Operation(summary = "Get vendor performance", description = "Get detailed performance metrics for a specific vendor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vendor performance retrieved successfully",
                    content = @Content(schema = @Schema(implementation = TopVendorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Vendor not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<TopVendorResponse> getVendorPerformance(
            @Parameter(description = "Vendor ID") @PathVariable Long vendorId,
            @Parameter(description = "Filter criteria") DashboardFilterRequest filter) {
        log.info("GET /v1/admin/dashboard/vendors/{}/performance", vendorId);
        TopVendorResponse performance = dashboardService.getVendorPerformance(vendorId, filter);
        return ResponseEntity.ok(performance);
    }

    @PostMapping("/export")
    @Operation(summary = "Export report", description = "Export dashboard data in CSV or Excel format")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Export job created successfully",
                    content = @Content(schema = @Schema(implementation = ExportJobResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid export request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ExportJobResponse> exportReport(
            @Valid @RequestBody ExportRequest request) {
        log.info("POST /v1/admin/dashboard/export - Report: {}, Format: {}",
                request.getReportType(), request.getFormat());
        ExportJobResponse response = dashboardService.exportReport(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/export/{jobId}/download")
    @Operation(summary = "Download exported report", description = "Download the exported report file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File downloaded successfully"),
            @ApiResponse(responseCode = "404", description = "Export job not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public void downloadExport(
            @Parameter(description = "Export job ID") @PathVariable String jobId,
            HttpServletResponse response) throws IOException {
        log.info("GET /v1/admin/dashboard/export/{}/download", jobId);
        byte[] exportData = dashboardService.getExportData(jobId);
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.xlsx");
        response.getOutputStream().write(exportData);
        response.getOutputStream().flush();
    }

    @GetMapping("/summary")
    @Operation(summary = "Get dashboard summary", description = "Get a quick summary of key metrics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Summary retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<DashboardMetricsResponse> getDashboardSummary() {
        log.info("GET /v1/admin/dashboard/summary");
        DashboardFilterRequest filter = DashboardFilterRequest.builder().build();
        DashboardMetricsResponse metrics = dashboardService.getDashboardMetrics(filter);
        return ResponseEntity.ok(metrics);
    }
}