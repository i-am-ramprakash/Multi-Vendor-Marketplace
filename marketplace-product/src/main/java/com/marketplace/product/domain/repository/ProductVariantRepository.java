package com.marketplace.product.domain.repository;

import com.marketplace.product.domain.entity.ProductVariant;

import java.util.List;
import java.util.Optional;

public interface ProductVariantRepository {

    ProductVariant save(ProductVariant variant);

    Optional<ProductVariant> findById(Long id);

    Optional<ProductVariant> findBySku(String sku);

    boolean existsBySku(String sku);

    List<ProductVariant> findByProductId(Long productId);

    List<ProductVariant> findByProductIdAndIsActiveTrue(Long productId);

    void delete(ProductVariant variant);
}