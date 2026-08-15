package com.marketplace.cart.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddToCartRequest {

    @NotNull(message = "Product ID is required")
    @Positive(message = "Product ID must be positive")
    private Long productId;

    private Long variantId;

    @NotBlank(message = "Product name is required")
    private String productName;

    private String variantName;

    @NotNull(message = "Unit price is required")
    @Positive(message = "Unit price must be positive")
    private java.math.BigDecimal unitPrice;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    private String imageUrl;

    @Min(value = 1, message = "Max quantity must be at least 1")
    private Integer maxQuantity;
}