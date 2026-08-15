package com.marketplace.product.infrastructure.persistence.repository;

import com.marketplace.product.infrastructure.persistence.entity.InventoryMovementJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryMovementJpaRepository extends JpaRepository<InventoryMovementJpaEntity, Long> {

    List<InventoryMovementJpaEntity> findByVariantId(Long variantId);

    List<InventoryMovementJpaEntity> findByVariantIdOrderByCreatedAtDesc(Long variantId);
}