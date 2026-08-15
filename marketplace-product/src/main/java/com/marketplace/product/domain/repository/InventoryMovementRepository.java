package com.marketplace.product.domain.repository;

import com.marketplace.product.domain.entity.InventoryMovement;

import java.util.List;

public interface InventoryMovementRepository {

    InventoryMovement save(InventoryMovement movement);

    List<InventoryMovement> findByVariantId(Long variantId);

    List<InventoryMovement> findByVariantIdOrderByCreatedAtDesc(Long variantId);

    List<InventoryMovement> findByProductId(Long productId);
}