package com.marketplace.product.infrastructure.persistence.repository;

import com.marketplace.product.infrastructure.persistence.entity.ProductVariantJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductVariantJpaRepository extends JpaRepository<ProductVariantJpaEntity, Long> {

    Optional<ProductVariantJpaEntity> findBySku(String sku);

    boolean existsBySku(String sku);

    List<ProductVariantJpaEntity> findByProductId(Long productId);

    List<ProductVariantJpaEntity> findByProductIdAndIsActiveTrue(Long productId);
}