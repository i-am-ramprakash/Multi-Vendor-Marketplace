package com.marketplace.product.infrastructure.persistence.repository;

import com.marketplace.product.domain.entity.InventoryMovement;
import com.marketplace.product.domain.repository.InventoryMovementRepository;
import com.marketplace.product.infrastructure.persistence.entity.InventoryMovementJpaEntity;
import com.marketplace.product.infrastructure.persistence.mapper.InventoryMovementPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class InventoryMovementRepositoryImpl implements InventoryMovementRepository {

    private final InventoryMovementJpaRepository jpaRepository;

    @Override
    public InventoryMovement save(InventoryMovement movement) {
        InventoryMovementJpaEntity jpa = InventoryMovementPersistenceMapper.toJpaEntity(movement);
        InventoryMovementJpaEntity saved = jpaRepository.save(jpa);
        return InventoryMovementPersistenceMapper.toDomain(saved);
    }

    @Override
    public List<InventoryMovement> findByVariantId(Long variantId) {
        return InventoryMovementPersistenceMapper.toDomainList(jpaRepository.findByVariantId(variantId));
    }

    @Override
    public List<InventoryMovement> findByVariantIdOrderByCreatedAtDesc(Long variantId) {
        return InventoryMovementPersistenceMapper.toDomainList(jpaRepository.findByVariantIdOrderByCreatedAtDesc(variantId));
    }

    @Override
    public List<InventoryMovement> findByProductId(Long productId) {
        return InventoryMovementPersistenceMapper.toDomainList(jpaRepository.findByVariantId(productId));
    }
}