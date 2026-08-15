package com.marketplace.product.api.controller;

import com.marketplace.product.application.dto.*;
import com.marketplace.product.application.service.ProductService;
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

import java.util.List;

@RestController
@RequestMapping("/v1/admin/products")
@RequiredArgsConstructor
@Tag(name = "Admin Product Management", description = "Admin product management endpoints")
@PreAuthorize("hasRole('ADMIN')")
public class AdminProductController {

    private final ProductService productService;

    @GetMapping("/pending")
    @Operation(summary = "Get pending products", description = "Returns all products pending approval")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pending products retrieved successfully", content = @Content(schema = @Schema(implementation = ProductListResponse.class)))
    })
    public ResponseEntity<ProductListResponse> getPendingProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        ProductListResponse response = productService.getPendingProducts(page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/rejected")
    @Operation(summary = "Get rejected products", description = "Returns all rejected products")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rejected products retrieved successfully", content = @Content(schema = @Schema(implementation = ProductListResponse.class)))
    })
    public ResponseEntity<ProductListResponse> getRejectedProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        ProductListResponse response = productService.getRejectedProducts(page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/approval-requests")
    @Operation(summary = "Get approval requests", description = "Returns all pending approval requests")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Approval requests retrieved successfully")
    })
    public ResponseEntity<List<ProductApprovalResponse>> getApprovalRequests() {
        List<ProductApprovalResponse> response = productService.getApprovalRequests();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{productId}/approve")
    @Operation(summary = "Approve product", description = "Approves a product for listing")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product approved successfully", content = @Content(schema = @Schema(implementation = ProductResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid product state", content = @Content(schema = @Schema(implementation = com.marketplace.product.api.advice.ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(schema = @Schema(implementation = com.marketplace.product.api.advice.ErrorResponse.class)))
    })
    public ResponseEntity<ProductResponse> approveProduct(
            @PathVariable Long productId,
            @RequestParam Long adminId,
            @RequestParam(required = false) String notes) {
        ProductResponse response = productService.approveProduct(productId, adminId, notes);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{productId}/reject")
    @Operation(summary = "Reject product", description = "Rejects a product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product rejected successfully", content = @Content(schema = @Schema(implementation = ProductResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid product state", content = @Content(schema = @Schema(implementation = com.marketplace.product.api.advice.ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(schema = @Schema(implementation = com.marketplace.product.api.advice.ErrorResponse.class)))
    })
    public ResponseEntity<ProductResponse> rejectProduct(
            @PathVariable Long productId,
            @RequestParam Long adminId,
            @RequestBody String reason) {
        ProductResponse response = productService.rejectProduct(productId, adminId, reason);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{productId}")
    @Operation(summary = "Get product", description = "Returns product details (Admin)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product retrieved successfully", content = @Content(schema = @Schema(implementation = ProductResponse.class))),
        @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(schema = @Schema(implementation = com.marketplace.product.api.advice.ErrorResponse.class)))
    })
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long productId) {
        ProductResponse response = productService.getProduct(productId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    @Operation(summary = "Get all products", description = "Returns all products with optional filters")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products retrieved successfully", content = @Content(schema = @Schema(implementation = ProductListResponse.class)))
    })
    public ResponseEntity<ProductListResponse> getAllProducts(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long vendorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        ProductSearchRequest request = ProductSearchRequest.builder()
            .status(status)
            .vendorId(vendorId)
            .page(page)
            .size(size)
            .build();

        ProductListResponse response = productService.searchProducts(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @Operation(summary = "Search products", description = "Search products with filters (Admin)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products retrieved successfully", content = @Content(schema = @Schema(implementation = ProductListResponse.class)))
    })
    public ResponseEntity<ProductListResponse> searchProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long vendorId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        ProductSearchRequest request = ProductSearchRequest.builder()
            .keyword(keyword)
            .status(status)
            .vendorId(vendorId)
            .categoryId(categoryId)
            .page(page)
            .size(size)
            .build();

        ProductListResponse response = productService.searchProducts(request);
        return ResponseEntity.ok(response);
    }
}