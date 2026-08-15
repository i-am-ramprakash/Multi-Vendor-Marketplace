package com.marketplace.order.domain.service;

public interface InventoryService {

    boolean checkAvailability(Long productId, Long variantId, int quantity);

    void reserveInventory(Long productId, Long variantId, int quantity, Long orderId);

    void releaseInventory(Long productId, Long variantId, int quantity, Long orderId);

    void deductInventory(Long productId, Long variantId, int quantity, Long orderId);
}