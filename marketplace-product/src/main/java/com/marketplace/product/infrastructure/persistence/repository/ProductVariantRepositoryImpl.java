package com.marketplace.product.infrastructure.persistence.repository;

import com.marketplace.product.domain.entity.ProductVariant;
import com.marketplace.product.domain.repository.ProductVariantRepository;
import com.marketplace.product.infrastructure.persistence.entity.ProductVariantJpaEntity;
import com.marketplace.product.infrastructure.persistence.mapper.ProductVariantPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductVariantRepositoryImpl implements ProductVariantRepository {

    private final ProductVariantJpaRepository jpaRepository;

    @Override
    public ProductVariant save(ProductVariant variant) {
        ProductVariantJpaEntity jpa = ProductVariantPersistenceMapper.toJpaEntity(variant);
        ProductVariantJpaEntity saved = jpaRepository.save(jpa);
        return ProductVariantPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<ProductVariant> findById(Long id) {
        return jpaRepository.findById(id)
            .map(ProductVariantPersistenceMapper::toDomain);
    }

    @Override
    public Optional<ProductVariant> findBySku(String sku) {
        return jpaRepository.findBySku(sku)
            .map(ProductVariantPersistenceMapper::toDomain);
    }

    @Override
    public boolean existsBySku(String sku) {
        return jpaRepository.existsBySku(sku);
    }

    @Override
    public List<ProductVariant> findByProductId(Long productId) {
        return ProductVariantPersistenceMapper.toDomainList(jpaRepository.findByProductId(productId));
    }

    @Override
    public List<ProductVariant> findByProductIdAndIsActiveTrue(Long productId) {
        return ProductVariantPersistenceMapper.toDomainList(jpaRepository.findByProductIdAndIsActiveTrue(productId));
    }

    @Override
    public void delete(ProductVariant variant) {
        jpaRepository.deleteById(variant.getId());
    }
}