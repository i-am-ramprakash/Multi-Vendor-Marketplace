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
@RequestMapping("/v1/products")
@RequiredArgsConstructor
@Tag(name = "Product Management", description = "Product and category management endpoints")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @Operation(summary = "Create product", description = "Creates a new product (Vendors only)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product created successfully", content = @Content(schema = @Schema(implementation = ProductResponse.class))),
        @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(implementation = com.marketplace.product.api.advice.ErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = "Product already exists", content = @Content(schema = @Schema(implementation = com.marketplace.product.api.advice.ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody CreateProductRequest request) {
        ProductResponse response = productService.createProduct(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{productId}")
    @Operation(summary = "Get product", description = "Returns product details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product retrieved successfully", content = @Content(schema = @Schema(implementation = ProductResponse.class))),
        @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(schema = @Schema(implementation = com.marketplace.product.api.advice.ErrorResponse.class)))
    })
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long productId) {
        ProductResponse response = productService.getProduct(productId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/slug/{slug}")
    @Operation(summary = "Get product by slug", description = "Returns product details by slug")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product retrieved successfully", content = @Content(schema = @Schema(implementation = ProductResponse.class))),
        @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(schema = @Schema(implementation = com.marketplace.product.api.advice.ErrorResponse.class)))
    })
    public ResponseEntity<ProductResponse> getProductBySlug(@PathVariable String slug) {
        ProductResponse response = productService.getProductBySlug(slug);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{productId}")
    @Operation(summary = "Update product", description = "Updates product information (Owner only)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product updated successfully", content = @Content(schema = @Schema(implementation = ProductResponse.class))),
        @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(implementation = com.marketplace.product.api.advice.ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(schema = @Schema(implementation = com.marketplace.product.api.advice.ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long productId,
            @RequestParam Long vendorId,
            @Valid @RequestBody UpdateProductRequest request) {
        ProductResponse response = productService.updateProduct(productId, vendorId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "Delete product", description = "Deletes a product (Owner only, draft/rejected only)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(schema = @Schema(implementation = com.marketplace.product.api.advice.ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long productId,
            @RequestParam Long vendorId) {
        productService.deleteProduct(productId, vendorId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search products", description = "Search and filter products with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products retrieved successfully", content = @Content(schema = @Schema(implementation = ProductListResponse.class)))
    })
    public ResponseEntity<ProductListResponse> searchProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long vendorId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String minPrice,
            @RequestParam(required = false) String maxPrice,
            @RequestParam(required = false) Boolean inStockOnly,
            @RequestParam(required = false) Boolean featuredOnly,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        
        ProductSearchRequest request = ProductSearchRequest.builder()
            .keyword(keyword)
            .categoryId(categoryId)
            .vendorId(vendorId)
            .status(status)
            .inStockOnly(inStockOnly)
            .featuredOnly(featuredOnly)
            .page(page)
            .size(size)
            .sortBy(sortBy)
            .sortDirection(sortDirection)
            .build();

        ProductListResponse response = productService.searchProducts(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/vendor/{vendorId}")
    @Operation(summary = "Get vendor products", description = "Returns products for a specific vendor")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products retrieved successfully", content = @Content(schema = @Schema(implementation = ProductListResponse.class)))
    })
    public ResponseEntity<ProductListResponse> getVendorProducts(
            @PathVariable Long vendorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        ProductListResponse response = productService.getVendorProducts(vendorId, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get category products", description = "Returns products for a specific category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products retrieved successfully", content = @Content(schema = @Schema(implementation = ProductListResponse.class)))
    })
    public ResponseEntity<ProductListResponse> getCategoryProducts(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        ProductListResponse response = productService.getCategoryProducts(categoryId, page, size);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{productId}/submit")
    @Operation(summary = "Submit for approval", description = "Submits product for admin approval")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product submitted successfully", content = @Content(schema = @Schema(implementation = ProductApprovalResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid product state", content = @Content(schema = @Schema(implementation = com.marketplace.product.api.advice.ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(schema = @Schema(implementation = com.marketplace.product.api.advice.ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<ProductApprovalResponse> submitForApproval(
            @PathVariable Long productId,
            @RequestParam Long vendorId) {
        ProductApprovalResponse response = productService.submitForApproval(productId, vendorId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{productId}/variants")
    @Operation(summary = "Add variant", description = "Adds a variant to a product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Variant added successfully", content = @Content(schema = @Schema(implementation = VariantResponse.class))),
        @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(schema = @Schema(implementation = com.marketplace.product.api.advice.ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<VariantResponse> addVariant(
            @PathVariable Long productId,
            @Valid @RequestBody CreateVariantRequest request) {
        VariantResponse response = productService.addVariant(productId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{productId}/variants")
    @Operation(summary = "Get variants", description = "Returns all variants for a product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Variants retrieved successfully")
    })
    public ResponseEntity<List<VariantResponse>> getVariants(@PathVariable Long productId) {
        List<VariantResponse> response = productService.getVariants(productId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/variants/{variantId}")
    @Operation(summary = "Update variant", description = "Updates a product variant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Variant updated successfully", content = @Content(schema = @Schema(implementation = VariantResponse.class))),
        @ApiResponse(responseCode = "404", description = "Variant not found", content = @Content(schema = @Schema(implementation = com.marketplace.product.api.advice.ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<VariantResponse> updateVariant(
            @PathVariable Long variantId,
            @Valid @RequestBody CreateVariantRequest request) {
        VariantResponse response = productService.updateVariant(variantId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/variants/{variantId}")
    @Operation(summary = "Delete variant", description = "Deletes a product variant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Variant deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Variant not found", content = @Content(schema = @Schema(implementation = com.marketplace.product.api.advice.ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<Void> deleteVariant(
            @PathVariable Long variantId,
            @RequestParam Long vendorId) {
        productService.deleteVariant(variantId, vendorId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/variants/{variantId}/inventory")
    @Operation(summary = "Update inventory", description = "Updates inventory for a variant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Inventory updated successfully", content = @Content(schema = @Schema(implementation = VariantResponse.class))),
        @ApiResponse(responseCode = "404", description = "Variant not found", content = @Content(schema = @Schema(implementation = com.marketplace.product.api.advice.ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<VariantResponse> updateInventory(
            @PathVariable Long variantId,
            @Valid @RequestBody UpdateInventoryRequest request) {
        VariantResponse response = productService.updateInventory(variantId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{productId}/images")
    @Operation(summary = "Get images", description = "Returns all images for a product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Images retrieved successfully")
    })
    public ResponseEntity<List<ImageResponse>> getImages(@PathVariable Long productId) {
        List<ImageResponse> response = productService.getImages(productId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{productId}/images")
    @Operation(summary = "Add image", description = "Adds an image to a product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Image added successfully", content = @Content(schema = @Schema(implementation = ImageResponse.class))),
        @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(schema = @Schema(implementation = com.marketplace.product.api.advice.ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<ImageResponse> addImage(
            @PathVariable Long productId,
            @Valid @RequestBody CreateImageRequest request) {
        ImageResponse response = productService.addImage(productId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/images/{imageId}")
    @Operation(summary = "Delete image", description = "Deletes a product image")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Image deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Image not found", content = @Content(schema = @Schema(implementation = com.marketplace.product.api.advice.ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<Void> deleteImage(
            @PathVariable Long imageId,
            @RequestParam Long vendorId) {
        productService.deleteImage(imageId, vendorId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{productId}/images/reorder")
    @Operation(summary = "Reorder images", description = "Reorders product images")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Images reordered successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(schema = @Schema(implementation = com.marketplace.product.api.advice.ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<Void> reorderImages(
            @PathVariable Long productId,
            @RequestBody List<Long> imageIds) {
        productService.reorderImages(productId, imageIds);
        return ResponseEntity.ok().build();
    }
}