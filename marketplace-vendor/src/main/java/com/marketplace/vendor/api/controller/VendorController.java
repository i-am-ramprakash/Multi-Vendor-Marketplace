package com.marketplace.vendor.api.controller;

import com.marketplace.vendor.application.dto.*;
import com.marketplace.vendor.application.service.VendorService;
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
@RequestMapping("/v1/vendors")
@RequiredArgsConstructor
@Tag(name = "Vendor Management", description = "Vendor store management endpoints")
public class VendorController {

    private final VendorService vendorService;

    @PostMapping("/register")
    @Operation(summary = "Register as vendor", description = "Creates a new vendor store application (requires approval)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registration successful", content = @Content(schema = @Schema(implementation = VendorResponse.class))),
        @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(implementation = com.marketplace.vendor.api.exception.ErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = "Vendor already exists", content = @Content(schema = @Schema(implementation = com.marketplace.vendor.api.exception.ErrorResponse.class)))
    })
    public ResponseEntity<VendorResponse> registerVendor(@Valid @RequestBody VendorRegistrationRequest request) {
        VendorResponse response = vendorService.registerVendor(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    @Operation(summary = "Get my vendor profile", description = "Returns the authenticated vendor's store profile")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profile retrieved successfully", content = @Content(schema = @Schema(implementation = VendorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Vendor not found", content = @Content(schema = @Schema(implementation = com.marketplace.vendor.api.exception.ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<VendorResponse> getMyProfile(@RequestParam Long userId) {
        VendorResponse response = vendorService.getVendorProfileByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{vendorId}")
    @Operation(summary = "Update vendor profile", description = "Updates the vendor's store profile")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profile updated successfully", content = @Content(schema = @Schema(implementation = VendorResponse.class))),
        @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(implementation = com.marketplace.vendor.api.exception.ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Vendor not found", content = @Content(schema = @Schema(implementation = com.marketplace.vendor.api.exception.ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<VendorResponse> updateProfile(
            @PathVariable Long vendorId,
            @Valid @RequestBody UpdateVendorProfileRequest request) {
        VendorResponse response = vendorService.updateVendorProfile(vendorId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{vendorId}")
    @Operation(summary = "Get vendor profile", description = "Returns a vendor's public store profile")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profile retrieved successfully", content = @Content(schema = @Schema(implementation = VendorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Vendor not found", content = @Content(schema = @Schema(implementation = com.marketplace.vendor.api.exception.ErrorResponse.class)))
    })
    public ResponseEntity<VendorResponse> getVendorProfile(@PathVariable Long vendorId) {
        VendorResponse response = vendorService.getVendorProfile(vendorId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/store/{storeSlug}")
    @Operation(summary = "Get vendor store by slug", description = "Returns a vendor's public store profile by store slug")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profile retrieved successfully", content = @Content(schema = @Schema(implementation = VendorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Store not found", content = @Content(schema = @Schema(implementation = com.marketplace.vendor.api.exception.ErrorResponse.class)))
    })
    public ResponseEntity<VendorResponse> getVendorByStoreSlug(@PathVariable String storeSlug) {
        VendorResponse response = vendorService.getVendorProfileByStoreSlug(storeSlug);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{vendorId}/dashboard")
    @Operation(summary = "Get vendor dashboard", description = "Returns vendor dashboard with sales data and analytics")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Dashboard retrieved successfully", content = @Content(schema = @Schema(implementation = VendorDashboardResponse.class))),
        @ApiResponse(responseCode = "404", description = "Vendor not found", content = @Content(schema = @Schema(implementation = com.marketplace.vendor.api.exception.ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<VendorDashboardResponse> getVendorDashboard(@PathVariable Long vendorId) {
        VendorDashboardResponse response = vendorService.getVendorDashboard(vendorId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{vendorId}/analytics")
    @Operation(summary = "Get vendor analytics", description = "Returns vendor analytics for a specific period")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Analytics retrieved successfully", content = @Content(schema = @Schema(implementation = VendorAnalyticsResponse.class))),
        @ApiResponse(responseCode = "404", description = "Vendor not found", content = @Content(schema = @Schema(implementation = com.marketplace.vendor.api.exception.ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<VendorAnalyticsResponse> getVendorAnalytics(
            @PathVariable Long vendorId,
            @RequestParam(defaultValue = "month") String period) {
        VendorAnalyticsResponse response = vendorService.getVendorAnalytics(vendorId, period);
        return ResponseEntity.ok(response);
    }
}