package com.marketplace.wishlist.api.controller;

import com.marketplace.wishlist.application.dto.*;
import com.marketplace.wishlist.application.service.WishlistService;
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
@RequestMapping("/v1/wishlist")
@RequiredArgsConstructor
@Tag(name = "Wishlist", description = "Wishlist management endpoints")
@PreAuthorize("hasRole('CUSTOMER')")
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping("/items")
    @Operation(summary = "Add item to wishlist", description = "Adds a product to the wishlist")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item added successfully", content = @Content(schema = @Schema(implementation = WishlistItemResponse.class))),
        @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(implementation = com.marketplace.wishlist.api.advice.ErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = "Duplicate item", content = @Content(schema = @Schema(implementation = com.marketplace.wishlist.api.advice.ErrorResponse.class)))
    })
    public ResponseEntity<WishlistItemResponse> addItem(
            @RequestParam Long userId,
            @Valid @RequestBody AddToWishlistRequest request) {
        WishlistItemResponse response = wishlistService.addToWishlist(userId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/items")
    @Operation(summary = "Remove item from wishlist", description = "Removes a product from the wishlist")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item removed successfully"),
        @ApiResponse(responseCode = "404", description = "Item not found", content = @Content(schema = @Schema(implementation = com.marketplace.wishlist.api.advice.ErrorResponse.class)))
    })
    public ResponseEntity<Void> removeItem(
            @RequestParam Long userId,
            @RequestParam Long productId,
            @RequestParam(required = false) Long variantId) {
        wishlistService.removeFromWishlist(userId, productId, variantId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/items/{itemId}")
    @Operation(summary = "Remove wishlist item by ID", description = "Removes a wishlist item by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item removed successfully"),
        @ApiResponse(responseCode = "404", description = "Item not found", content = @Content(schema = @Schema(implementation = com.marketplace.wishlist.api.advice.ErrorResponse.class)))
    })
    public ResponseEntity<Void> removeItemById(
            @RequestParam Long userId,
            @PathVariable Long itemId) {
        wishlistService.removeWishlistItemById(userId, itemId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "Get wishlist", description = "Returns the user's wishlist with all items")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Wishlist retrieved successfully", content = @Content(schema = @Schema(implementation = WishlistResponse.class))),
        @ApiResponse(responseCode = "404", description = "Wishlist not found", content = @Content(schema = @Schema(implementation = com.marketplace.wishlist.api.advice.ErrorResponse.class)))
    })
    public ResponseEntity<WishlistResponse> getWishlist(@RequestParam Long userId) {
        WishlistResponse response = wishlistService.getWishlist(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/summary")
    @Operation(summary = "Get wishlist summary", description = "Returns a lightweight summary of the wishlist")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Wishlist summary retrieved successfully", content = @Content(schema = @Schema(implementation = WishlistSummaryResponse.class)))
    })
    public ResponseEntity<WishlistSummaryResponse> getWishlistSummary(@RequestParam Long userId) {
        WishlistSummaryResponse response = wishlistService.getWishlistSummary(userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    @Operation(summary = "Clear wishlist", description = "Removes all items from the wishlist")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Wishlist cleared successfully")
    })
    public ResponseEntity<Void> clearWishlist(@RequestParam Long userId) {
        wishlistService.clearWishlist(userId);
        return ResponseEntity.ok().build();
    }
}