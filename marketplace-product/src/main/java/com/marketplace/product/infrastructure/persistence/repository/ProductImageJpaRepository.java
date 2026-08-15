package com.marketplace.product.infrastructure.persistence.repository;

import com.marketplace.product.infrastructure.persistence.entity.ProductImageJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductImageJpaRepository extends JpaRepository<ProductImageJpaEntity, Long> {

    List<ProductImageJpaEntity> findByProductId(Long productId);

    List<ProductImageJpaEntity> findByProductIdOrderByPositionAsc(Long productId);

    List<ProductImageJpaEntity> findByVariantId(Long variantId);

    Optional<ProductImageJpaEntity> findByProductIdAndIsPrimaryTrue(Long productId);

    void deleteAllByProductId(Long productId);
}