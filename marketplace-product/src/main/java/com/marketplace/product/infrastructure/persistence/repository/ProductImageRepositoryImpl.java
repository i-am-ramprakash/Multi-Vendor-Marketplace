package com.marketplace.product.infrastructure.persistence.repository;

import com.marketplace.product.domain.entity.ProductImage;
import com.marketplace.product.domain.repository.ProductImageRepository;
import com.marketplace.product.infrastructure.persistence.entity.ProductImageJpaEntity;
import com.marketplace.product.infrastructure.persistence.mapper.ProductImagePersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductImageRepositoryImpl implements ProductImageRepository {

    private final ProductImageJpaRepository jpaRepository;

    @Override
    public ProductImage save(ProductImage image) {
        ProductImageJpaEntity jpa = ProductImagePersistenceMapper.toJpaEntity(image);
        ProductImageJpaEntity saved = jpaRepository.save(jpa);
        return ProductImagePersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<ProductImage> findById(Long id) {
        return jpaRepository.findById(id)
            .map(ProductImagePersistenceMapper::toDomain);
    }

    @Override
    public List<ProductImage> findByProductId(Long productId) {
        return ProductImagePersistenceMapper.toDomainList(jpaRepository.findByProductId(productId));
    }

    @Override
    public List<ProductImage> findByProductIdOrderByPositionAsc(Long productId) {
        return ProductImagePersistenceMapper.toDomainList(jpaRepository.findByProductIdOrderByPositionAsc(productId));
    }

    @Override
    public List<ProductImage> findByVariantId(Long variantId) {
        return ProductImagePersistenceMapper.toDomainList(jpaRepository.findByVariantId(variantId));
    }

    @Override
    public Optional<ProductImage> findByProductIdAndIsPrimaryTrue(Long productId) {
        return jpaRepository.findByProductIdAndIsPrimaryTrue(productId)
            .map(ProductImagePersistenceMapper::toDomain);
    }

    @Override
    public void delete(ProductImage image) {
        jpaRepository.deleteById(image.getId());
    }

    @Override
    public void deleteAllByProductId(Long productId) {
        jpaRepository.deleteAllByProductId(productId);
    }
}