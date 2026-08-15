package com.marketplace.commission.api.controller;

import com.marketplace.commission.application.dto.*;
import com.marketplace.commission.application.service.CommissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@RestController
@RequestMapping("/v1/commissions")
@RequiredArgsConstructor
@Tag(name = "Commission Management", description = "Commission management endpoints")
@PreAuthorize("hasAnyRole('VENDOR', 'ADMIN')")
public class CommissionController {

    private final CommissionService commissionService;

    @PostMapping("/calculate")
    @Operation(summary = "Calculate commission", description = "Calculates commission for an order item")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Commission calculated successfully", content = @Content(schema = @Schema(implementation = CommissionRecordResponse.class))),
        @ApiResponse(responseCode = "404", description = "No applicable commission rule found", content = @Content(schema = @Schema(implementation = com.marketplace.commission.api.advice.ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommissionRecordResponse> calculateCommission(
            @RequestParam Long orderId,
            @RequestParam Long orderItemId,
            @RequestParam Long vendorId,
            @RequestParam Long categoryId,
            @RequestParam BigDecimal orderAmount,
            @RequestParam(defaultValue = "USD") String currency) {
        CommissionRecordResponse response = commissionService.calculateCommission(
            orderId, orderItemId, vendorId, categoryId, orderAmount, currency);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/vendor/{vendorId}/earnings")
    @Operation(summary = "Get vendor earnings", description = "Returns total earnings for a vendor")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Earnings retrieved successfully", content = @Content(schema = @Schema(implementation = VendorEarningsResponse.class)))
    })
    public ResponseEntity<VendorEarningsResponse> getVendorEarnings(@PathVariable Long vendorId) {
        VendorEarningsResponse response = commissionService.getVendorEarnings(vendorId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/vendor/{vendorId}/earnings/period")
    @Operation(summary = "Get vendor earnings for period", description = "Returns earnings for a vendor within a date range")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Earnings retrieved successfully", content = @Content(schema = @Schema(implementation = VendorEarningsResponse.class)))
    })
    public ResponseEntity<VendorEarningsResponse> getVendorEarningsForPeriod(
            @PathVariable Long vendorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        Instant startInstant = start.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant endInstant = end.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();
        VendorEarningsResponse response = commissionService.getVendorEarnings(vendorId, startInstant, endInstant);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/vendor/{vendorId}/records")
    @Operation(summary = "Get vendor commission records", description = "Returns paginated commission records for a vendor")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Records retrieved successfully", content = @Content(schema = @Schema(implementation = CommissionRecordResponse.class)))
    })
    public ResponseEntity<List<CommissionRecordResponse>> getVendorRecords(
            @PathVariable Long vendorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<CommissionRecordResponse> response = commissionService.getVendorRecords(vendorId, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/vendor/{vendorId}/records/unsettled")
    @Operation(summary = "Get vendor unsettled records", description = "Returns unsettled commission records for a vendor")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Records retrieved successfully", content = @Content(schema = @Schema(implementation = CommissionRecordResponse.class)))
    })
    public ResponseEntity<List<CommissionRecordResponse>> getVendorUnsettledRecords(@PathVariable Long vendorId) {
        List<CommissionRecordResponse> response = commissionService.getVendorUnsettledRecords(vendorId);
        return ResponseEntity.ok(response);
    }
}