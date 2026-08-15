package com.marketplace.commission.api.controller;

import com.marketplace.commission.application.dto.*;
import com.marketplace.commission.application.service.CommissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@RestController
@RequestMapping("/v1/admin/commissions")
@RequiredArgsConstructor
@Tag(name = "Admin Commission Management", description = "Admin commission management endpoints")
@PreAuthorize("hasRole('ADMIN')")
public class AdminCommissionController {

    private final CommissionService commissionService;

    @PostMapping("/rules")
    @Operation(summary = "Create commission rule", description = "Creates a new commission rule")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Rule created successfully", content = @Content(schema = @Schema(implementation = CommissionRuleResponse.class))),
        @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(implementation = com.marketplace.commission.api.advice.ErrorResponse.class)))
    })
    public ResponseEntity<CommissionRuleResponse> createCommissionRule(@Valid @RequestBody CreateCommissionRuleRequest request) {
        CommissionRuleResponse response = commissionService.createCommissionRule(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/settlements")
    @Operation(summary = "Create settlement", description = "Creates a new settlement for a vendor")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Settlement created successfully", content = @Content(schema = @Schema(implementation = SettlementResponse.class))),
        @ApiResponse(responseCode = "400", description = "No unsettled records found", content = @Content(schema = @Schema(implementation = com.marketplace.commission.api.advice.ErrorResponse.class)))
    })
    public ResponseEntity<SettlementResponse> createSettlement(
            @RequestParam Long vendorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodStart,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodEnd) {
        Instant startInstant = periodStart.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant endInstant = periodEnd.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();
        SettlementResponse response = commissionService.createSettlement(vendorId, startInstant, endInstant);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/settlements/{settlementId}/process")
    @Operation(summary = "Process settlement", description = "Starts processing a settlement")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Settlement processing started", content = @Content(schema = @Schema(implementation = SettlementResponse.class))),
        @ApiResponse(responseCode = "404", description = "Settlement not found", content = @Content(schema = @Schema(implementation = com.marketplace.commission.api.advice.ErrorResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid settlement state", content = @Content(schema = @Schema(implementation = com.marketplace.commission.api.advice.ErrorResponse.class)))
    })
    public ResponseEntity<SettlementResponse> processSettlement(
            @PathVariable Long settlementId,
            @RequestParam Long performedBy) {
        SettlementResponse response = commissionService.processSettlement(settlementId, performedBy);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/settlements/{settlementId}/complete")
    @Operation(summary = "Complete settlement", description = "Marks a settlement as completed")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Settlement completed", content = @Content(schema = @Schema(implementation = SettlementResponse.class))),
        @ApiResponse(responseCode = "404", description = "Settlement not found", content = @Content(schema = @Schema(implementation = com.marketplace.commission.api.advice.ErrorResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid settlement state", content = @Content(schema = @Schema(implementation = com.marketplace.commission.api.advice.ErrorResponse.class)))
    })
    public ResponseEntity<SettlementResponse> completeSettlement(
            @PathVariable Long settlementId,
            @RequestParam String paymentReference,
            @RequestParam Long performedBy) {
        SettlementResponse response = commissionService.completeSettlement(settlementId, paymentReference, performedBy);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/revenue/monthly")
    @Operation(summary = "Get monthly revenue", description = "Returns monthly revenue summary")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Revenue retrieved successfully", content = @Content(schema = @Schema(implementation = MonthlyRevenueResponse.class)))
    })
    public ResponseEntity<MonthlyRevenueResponse> getMonthlyRevenue(
            @RequestParam int year,
            @RequestParam int month) {
        MonthlyRevenueResponse response = commissionService.getMonthlyRevenue(year, month);
        return ResponseEntity.ok(response);
    }
}