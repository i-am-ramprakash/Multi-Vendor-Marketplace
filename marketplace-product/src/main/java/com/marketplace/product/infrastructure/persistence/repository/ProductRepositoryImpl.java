package com.marketplace.product.infrastructure.persistence.repository;

import com.marketplace.product.domain.entity.Product;
import com.marketplace.product.domain.repository.ProductRepository;
import com.marketplace.product.domain.valueobject.ProductStatus;
import com.marketplace.product.infrastructure.persistence.entity.ProductJpaEntity;
import com.marketplace.product.infrastructure.persistence.mapper.ProductPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository jpaRepository;

    @Override
    public Product save(Product product) {
        ProductJpaEntity jpa = ProductPersistenceMapper.toJpaEntity(product);
        ProductJpaEntity saved = jpaRepository.save(jpa);
        return ProductPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return jpaRepository.findById(id)
            .map(ProductPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Product> findBySlug(String slug) {
        return jpaRepository.findBySlug(slug)
            .map(ProductPersistenceMapper::toDomain);
    }

    @Override
    public boolean existsBySlug(String slug) {
        return jpaRepository.existsBySlug(slug);
    }

    @Override
    public List<Product> findAll() {
        return jpaRepository.findAll().stream()
            .map(ProductPersistenceMapper::toDomain)
            .toList();
    }

    @Override
    public List<Product> findByVendorId(Long vendorId) {
        return jpaRepository.findByVendorId(vendorId).stream()
            .map(ProductPersistenceMapper::toDomain)
            .toList();
    }

    @Override
    public List<Product> findByCategoryId(Long categoryId) {
        return jpaRepository.findByCategoryId(categoryId).stream()
            .map(ProductPersistenceMapper::toDomain)
            .toList();
    }

    @Override
    public List<Product> findByStatus(ProductStatus status) {
        return jpaRepository.findByStatus(status).stream()
            .map(ProductPersistenceMapper::toDomain)
            .toList();
    }

    @Override
    public List<Product> findByVendorIdAndStatus(Long vendorId, ProductStatus status) {
        return jpaRepository.findByVendorIdAndStatus(vendorId, status).stream()
            .map(ProductPersistenceMapper::toDomain)
            .toList();
    }

    @Override
    public Page<Product> findByCategoryIdAndStatus(Long categoryId, ProductStatus status, Pageable pageable) {
        return jpaRepository.findByCategoryIdAndStatus(categoryId, status, pageable)
            .map(ProductPersistenceMapper::toDomain);
    }

    @Override
    public Page<Product> findByStatus(ProductStatus status, Pageable pageable) {
        return jpaRepository.findByStatus(status, pageable)
            .map(ProductPersistenceMapper::toDomain);
    }

    @Override
    public Page<Product> searchByNameContaining(String keyword, Pageable pageable) {
        return jpaRepository.search(keyword, null, null, null, pageable)
            .map(ProductPersistenceMapper::toDomain);
    }

    @Override
    public Page<Product> search(String keyword, Long categoryId, Long vendorId, 
                                ProductStatus status, Pageable pageable) {
        return jpaRepository.search(keyword, categoryId, vendorId, status, pageable)
            .map(ProductPersistenceMapper::toDomain);
    }

    @Override
    public long countByVendorId(Long vendorId) {
        return jpaRepository.countByVendorId(vendorId);
    }

    @Override
    public long countByCategoryId(Long categoryId) {
        return jpaRepository.countByCategoryId(categoryId);
    }

    @Override
    public long countByStatus(ProductStatus status) {
        return jpaRepository.countByStatus(status);
    }

    @Override
    public void delete(Product product) {
        jpaRepository.deleteById(product.getId());
    }
}