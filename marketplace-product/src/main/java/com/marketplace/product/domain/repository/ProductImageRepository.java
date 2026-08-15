package com.marketplace.product.domain.repository;

import com.marketplace.product.domain.entity.ProductImage;

import java.util.List;
import java.util.Optional;

public interface ProductImageRepository {

    ProductImage save(ProductImage image);

    Optional<ProductImage> findById(Long id);

    List<ProductImage> findByProductId(Long productId);

    List<ProductImage> findByProductIdOrderByPositionAsc(Long productId);

    List<ProductImage> findByVariantId(Long variantId);

    Optional<ProductImage> findByProductIdAndIsPrimaryTrue(Long productId);

    void delete(ProductImage image);

    void deleteAllByProductId(Long productId);
}