package com.marketplace.cart.domain.service;

public interface InventoryValidationService {

    boolean isProductAvailable(Long productId, Long variantId, int quantity);

    int getAvailableQuantity(Long productId, Long variantId);

    int getMaxAllowedQuantity(Long productId, Long variantId);
}