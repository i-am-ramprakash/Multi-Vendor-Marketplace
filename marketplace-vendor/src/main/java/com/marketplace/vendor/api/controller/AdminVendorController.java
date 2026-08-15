package com.marketplace.vendor.api.controller;

import com.marketplace.vendor.application.dto.*;
import com.marketplace.vendor.application.service.VendorService;
import com.marketplace.vendor.domain.valueobject.VendorStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/admin/vendors")
@RequiredArgsConstructor
@Tag(name = "Admin Vendor Management", description = "Admin vendor management endpoints")
public class AdminVendorController {

    private final VendorService vendorService;

    @GetMapping
    @Operation(summary = "Get all vendors", description = "Returns a list of all vendors with optional filtering by status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vendors retrieved successfully", content = @Content(schema = @Schema(implementation = VendorListResponse.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VendorListResponse> getVendors(
            @RequestParam(required = false) VendorStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        VendorListResponse response = vendorService.getVendorsByStatus(status, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{vendorId}")
    @Operation(summary = "Get vendor details", description = "Returns detailed vendor information for admin review")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vendor retrieved successfully", content = @Content(schema = @Schema(implementation = VendorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Vendor not found", content = @Content(schema = @Schema(implementation = com.marketplace.vendor.api.exception.ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VendorResponse> getVendor(@PathVariable Long vendorId) {
        VendorResponse response = vendorService.getVendorProfile(vendorId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{vendorId}/approve")
    @Operation(summary = "Approve vendor", description = "Approves a vendor application")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vendor approved successfully", content = @Content(schema = @Schema(implementation = VendorResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid vendor state", content = @Content(schema = @Schema(implementation = com.marketplace.vendor.api.exception.ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Vendor not found", content = @Content(schema = @Schema(implementation = com.marketplace.vendor.api.exception.ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VendorResponse> approveVendor(
            @PathVariable Long vendorId,
            @RequestParam(required = false) java.math.BigDecimal customCommissionRate,
            @RequestParam Long approvedBy) {
        VendorApprovalRequest request = VendorApprovalRequest.builder()
            .vendorId(vendorId)
            .approvedBy(approvedBy)
            .customCommissionRate(customCommissionRate)
            .build();
        VendorResponse response = vendorService.approveVendor(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{vendorId}/reject")
    @Operation(summary = "Reject vendor", description = "Rejects a vendor application with a reason")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vendor rejected successfully", content = @Content(schema = @Schema(implementation = VendorResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid vendor state", content = @Content(schema = @Schema(implementation = com.marketplace.vendor.api.exception.ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Vendor not found", content = @Content(schema = @Schema(implementation = com.marketplace.vendor.api.exception.ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VendorResponse> rejectVendor(
            @PathVariable Long vendorId,
            @RequestParam Long rejectedBy,
            @RequestParam String rejectionReason) {
        VendorRejectionRequest request = VendorRejectionRequest.builder()
            .vendorId(vendorId)
            .rejectedBy(rejectedBy)
            .rejectionReason(rejectionReason)
            .build();
        VendorResponse response = vendorService.rejectVendor(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{vendorId}/suspend")
    @Operation(summary = "Suspend vendor", description = "Suspends an active vendor")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vendor suspended successfully", content = @Content(schema = @Schema(implementation = VendorResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid vendor state", content = @Content(schema = @Schema(implementation = com.marketplace.vendor.api.exception.ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Vendor not found", content = @Content(schema = @Schema(implementation = com.marketplace.vendor.api.exception.ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VendorResponse> suspendVendor(
            @PathVariable Long vendorId,
            @RequestParam Long suspendedBy,
            @RequestParam String suspensionReason) {
        VendorSuspensionRequest request = VendorSuspensionRequest.builder()
            .vendorId(vendorId)
            .suspendedBy(suspendedBy)
            .suspensionReason(suspensionReason)
            .build();
        VendorResponse response = vendorService.suspendVendor(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{vendorId}/reactivate")
    @Operation(summary = "Reactivate vendor", description = "Reactivates a suspended vendor")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vendor reactivated successfully", content = @Content(schema = @Schema(implementation = VendorResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid vendor state", content = @Content(schema = @Schema(implementation = com.marketplace.vendor.api.exception.ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Vendor not found", content = @Content(schema = @Schema(implementation = com.marketplace.vendor.api.exception.ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VendorResponse> reactivateVendor(
            @PathVariable Long vendorId,
            @RequestParam Long reactivatedBy) {
        VendorResponse response = vendorService.reactivateVendor(vendorId, reactivatedBy);
        return ResponseEntity.ok(response);
    }
}