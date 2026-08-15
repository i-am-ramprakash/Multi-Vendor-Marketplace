package com.marketplace.product.api.controller;

import com.marketplace.product.application.dto.CategoryResponse;
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
@RequestMapping("/v1/categories")
@RequiredArgsConstructor
@Tag(name = "Category Management", description = "Category management endpoints")
public class CategoryController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "Get all categories", description = "Returns all active categories")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categories retrieved successfully")
    })
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> response = productService.getAllCategories();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/root")
    @Operation(summary = "Get root categories", description = "Returns top-level categories")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Root categories retrieved successfully")
    })
    public ResponseEntity<List<CategoryResponse>> getRootCategories() {
        List<CategoryResponse> response = productService.getRootCategories();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{categoryId}")
    @Operation(summary = "Get category", description = "Returns category details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Category retrieved successfully", content = @Content(schema = @Schema(implementation = CategoryResponse.class))),
        @ApiResponse(responseCode = "404", description = "Category not found", content = @Content(schema = @Schema(implementation = com.marketplace.product.api.advice.ErrorResponse.class)))
    })
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable Long categoryId) {
        CategoryResponse response = productService.getCategory(categoryId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{categoryId}/children")
    @Operation(summary = "Get child categories", description = "Returns child categories")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Child categories retrieved successfully")
    })
    public ResponseEntity<List<CategoryResponse>> getChildCategories(@PathVariable Long categoryId) {
        List<CategoryResponse> response = productService.getChildCategories(categoryId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{categoryId}/ancestors")
    @Operation(summary = "Get category ancestors", description = "Returns category path from root")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ancestors retrieved successfully")
    })
    public ResponseEntity<List<CategoryResponse>> getCategoryAncestors(@PathVariable Long categoryId) {
        List<CategoryResponse> response = productService.getCategoryAncestors(categoryId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create category", description = "Creates a new category (Admin only)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Category created successfully", content = @Content(schema = @Schema(implementation = CategoryResponse.class))),
        @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(implementation = com.marketplace.product.api.advice.ErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = "Category already exists", content = @Content(schema = @Schema(implementation = com.marketplace.product.api.advice.ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> createCategory(
            @Valid @RequestBody CategoryResponse request) {
        CategoryResponse response = productService.createCategory(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{categoryId}")
    @Operation(summary = "Update category", description = "Updates category (Admin only)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Category updated successfully", content = @Content(schema = @Schema(implementation = CategoryResponse.class))),
        @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(implementation = com.marketplace.product.api.advice.ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Category not found", content = @Content(schema = @Schema(implementation = com.marketplace.product.api.advice.ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable Long categoryId,
            @Valid @RequestBody CategoryResponse request) {
        CategoryResponse response = productService.updateCategory(categoryId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{categoryId}")
    @Operation(summary = "Delete category", description = "Deletes a category (Admin only, no products)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Category deleted successfully"),
        @ApiResponse(responseCode = "400", description = "Category has products or children", content = @Content(schema = @Schema(implementation = com.marketplace.product.api.advice.ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Category not found", content = @Content(schema = @Schema(implementation = com.marketplace.product.api.advice.ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
        productService.deleteCategory(categoryId);
        return ResponseEntity.ok().build();
    }
}