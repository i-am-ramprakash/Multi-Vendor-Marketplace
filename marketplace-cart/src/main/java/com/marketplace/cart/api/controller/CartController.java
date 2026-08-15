package com.marketplace.cart.api.controller;

import com.marketplace.cart.application.dto.*;
import com.marketplace.cart.application.service.CartService;
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
@RequestMapping("/v1/cart")
@RequiredArgsConstructor
@Tag(name = "Shopping Cart", description = "Shopping cart management endpoints")
@PreAuthorize("hasRole('CUSTOMER')")
public class CartController {

    private final CartService cartService;

    @PostMapping("/items")
    @Operation(summary = "Add item to cart", description = "Adds a product to the shopping cart")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item added successfully", content = @Content(schema = @Schema(implementation = CartItemResponse.class))),
        @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(implementation = com.marketplace.cart.api.advice.ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(schema = @Schema(implementation = com.marketplace.cart.api.advice.ErrorResponse.class)))
    })
    public ResponseEntity<CartItemResponse> addItem(
            @RequestParam Long userId,
            @Valid @RequestBody AddToCartRequest request) {
        CartItemResponse response = cartService.addToCart(userId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/items")
    @Operation(summary = "Remove item from cart", description = "Removes a product from the shopping cart")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item removed successfully"),
        @ApiResponse(responseCode = "404", description = "Item not found", content = @Content(schema = @Schema(implementation = com.marketplace.cart.api.advice.ErrorResponse.class)))
    })
    public ResponseEntity<Void> removeItem(
            @RequestParam Long userId,
            @RequestParam Long productId,
            @RequestParam(required = false) Long variantId) {
        cartService.removeFromCart(userId, productId, variantId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/items/{itemId}")
    @Operation(summary = "Remove cart item by ID", description = "Removes a cart item by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item removed successfully"),
        @ApiResponse(responseCode = "404", description = "Item not found", content = @Content(schema = @Schema(implementation = com.marketplace.cart.api.advice.ErrorResponse.class)))
    })
    public ResponseEntity<Void> removeItemById(
            @RequestParam Long userId,
            @PathVariable Long itemId) {
        cartService.removeCartItemById(userId, itemId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/items")
    @Operation(summary = "Update item quantity", description = "Updates the quantity of a cart item")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item updated successfully", content = @Content(schema = @Schema(implementation = CartItemResponse.class))),
        @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(implementation = com.marketplace.cart.api.advice.ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Item not found", content = @Content(schema = @Schema(implementation = com.marketplace.cart.api.advice.ErrorResponse.class)))
    })
    public ResponseEntity<CartItemResponse> updateItemQuantity(
            @RequestParam Long userId,
            @RequestParam Long productId,
            @RequestParam(required = false) Long variantId,
            @Valid @RequestBody UpdateCartItemRequest request) {
        CartItemResponse response = cartService.updateCartItem(userId, productId, variantId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get cart", description = "Returns the user's shopping cart with all items")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cart retrieved successfully", content = @Content(schema = @Schema(implementation = CartResponse.class))),
        @ApiResponse(responseCode = "404", description = "Cart not found", content = @Content(schema = @Schema(implementation = com.marketplace.cart.api.advice.ErrorResponse.class)))
    })
    public ResponseEntity<CartResponse> getCart(@RequestParam Long userId) {
        CartResponse response = cartService.getCart(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/summary")
    @Operation(summary = "Get cart summary", description = "Returns a lightweight summary of the cart")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cart summary retrieved successfully", content = @Content(schema = @Schema(implementation = CartSummaryResponse.class)))
    })
    public ResponseEntity<CartSummaryResponse> getCartSummary(@RequestParam Long userId) {
        CartSummaryResponse response = cartService.getCartSummary(userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    @Operation(summary = "Clear cart", description = "Removes all items from the shopping cart")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cart cleared successfully")
    })
    public ResponseEntity<Void> clearCart(@RequestParam Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok().build();
    }
}