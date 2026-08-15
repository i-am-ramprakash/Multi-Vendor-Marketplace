package com.marketplace.order.api.controller;

import com.marketplace.order.application.dto.*;
import com.marketplace.order.application.service.OrderService;
import com.marketplace.order.domain.valueobject.OrderStatus;
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
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
@Tag(name = "Order Management", description = "Order management endpoints")
@PreAuthorize("hasAnyRole('CUSTOMER', 'VENDOR', 'ADMIN')")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout")
    @Operation(summary = "Checkout", description = "Creates a new order from cart items")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order created successfully", content = @Content(schema = @Schema(implementation = OrderResponse.class))),
        @ApiResponse(responseCode = "400", description = "Validation error or empty cart", content = @Content(schema = @Schema(implementation = com.marketplace.order.api.advice.ErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = "Insufficient inventory", content = @Content(schema = @Schema(implementation = com.marketplace.order.api.advice.ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<OrderResponse> checkout(@Valid @RequestBody CheckoutRequest request) {
        OrderResponse response = orderService.checkout(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Get order", description = "Returns order details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order retrieved successfully", content = @Content(schema = @Schema(implementation = OrderResponse.class))),
        @ApiResponse(responseCode = "404", description = "Order not found", content = @Content(schema = @Schema(implementation = com.marketplace.order.api.advice.ErrorResponse.class)))
    })
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long orderId) {
        OrderResponse response = orderService.getOrder(orderId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/number/{orderNumber}")
    @Operation(summary = "Get order by number", description = "Returns order details by order number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order retrieved successfully", content = @Content(schema = @Schema(implementation = OrderResponse.class))),
        @ApiResponse(responseCode = "404", description = "Order not found", content = @Content(schema = @Schema(implementation = com.marketplace.order.api.advice.ErrorResponse.class)))
    })
    public ResponseEntity<OrderResponse> getOrderByNumber(@PathVariable String orderNumber) {
        OrderResponse response = orderService.getOrderByOrderNumber(orderNumber);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get user orders", description = "Returns paginated list of user orders")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Orders retrieved successfully", content = @Content(schema = @Schema(implementation = OrderListResponse.class)))
    })
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<OrderListResponse> getUserOrders(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        OrderListResponse response = orderService.getUserOrders(userId, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}/status/{status}")
    @Operation(summary = "Get user orders by status", description = "Returns paginated list of user orders with specific status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Orders retrieved successfully", content = @Content(schema = @Schema(implementation = OrderListResponse.class)))
    })
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<OrderListResponse> getUserOrdersByStatus(
            @PathVariable Long userId,
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        OrderStatus orderStatus = OrderStatus.valueOf(status);
        OrderListResponse response = orderService.getUserOrdersByStatus(userId, orderStatus, page, size);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{orderId}/cancel")
    @Operation(summary = "Cancel order", description = "Cancels an order")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order cancelled successfully", content = @Content(schema = @Schema(implementation = OrderResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid order state", content = @Content(schema = @Schema(implementation = com.marketplace.order.api.advice.ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Order not found", content = @Content(schema = @Schema(implementation = com.marketplace.order.api.advice.ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<OrderResponse> cancelOrder(
            @PathVariable Long orderId,
            @RequestParam Long userId,
            @RequestParam String reason) {
        OrderResponse response = orderService.cancelOrder(orderId, userId, reason);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{orderId}/status")
    @Operation(summary = "Update order status", description = "Updates order status (Vendor/Admin)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order status updated successfully", content = @Content(schema = @Schema(implementation = OrderResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid status transition", content = @Content(schema = @Schema(implementation = com.marketplace.order.api.advice.ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Order not found", content = @Content(schema = @Schema(implementation = com.marketplace.order.api.advice.ErrorResponse.class)))
    })
    @PreAuthorize("hasAnyRole('VENDOR', 'ADMIN')")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam String status,
            @RequestParam Long performedBy) {
        OrderResponse response = orderService.updateOrderStatus(orderId, status, performedBy);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/vendor/{vendorId}")
    @Operation(summary = "Get vendor orders", description = "Returns paginated list of vendor orders")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Orders retrieved successfully", content = @Content(schema = @Schema(implementation = OrderListResponse.class)))
    })
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<OrderListResponse> getVendorOrders(
            @PathVariable Long vendorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        OrderListResponse response = orderService.getVendorOrders(vendorId, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/vendor/{vendorId}/status/{status}")
    @Operation(summary = "Get vendor orders by status", description = "Returns paginated list of vendor orders with specific status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Orders retrieved successfully", content = @Content(schema = @Schema(implementation = OrderListResponse.class)))
    })
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<OrderListResponse> getVendorOrdersByStatus(
            @PathVariable Long vendorId,
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        OrderStatus orderStatus = OrderStatus.valueOf(status);
        OrderListResponse response = orderService.getVendorOrdersByStatus(vendorId, orderStatus, page, size);
        return ResponseEntity.ok(response);
    }
}