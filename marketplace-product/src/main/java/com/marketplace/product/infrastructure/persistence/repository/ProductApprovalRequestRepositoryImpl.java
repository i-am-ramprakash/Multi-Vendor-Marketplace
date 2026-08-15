package com.marketplace.product.infrastructure.persistence.repository;

import com.marketplace.product.domain.entity.ProductApprovalRequest;
import com.marketplace.product.domain.repository.ProductApprovalRequestRepository;
import com.marketplace.product.infrastructure.persistence.entity.ProductApprovalRequestJpaEntity;
import com.marketplace.product.infrastructure.persistence.mapper.ProductApprovalRequestPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductApprovalRequestRepositoryImpl implements ProductApprovalRequestRepository {

    private final ProductApprovalRequestJpaRepository jpaRepository;

    @Override
    public ProductApprovalRequest save(ProductApprovalRequest request) {
        ProductApprovalRequestJpaEntity jpa = ProductApprovalRequestPersistenceMapper.toJpaEntity(request);
        ProductApprovalRequestJpaEntity saved = jpaRepository.save(jpa);
        return ProductApprovalRequestPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<ProductApprovalRequest> findById(Long id) {
        return jpaRepository.findById(id)
            .map(ProductApprovalRequestPersistenceMapper::toDomain);
    }

    @Override
    public List<ProductApprovalRequest> findByProductId(Long productId) {
        return jpaRepository.findByProductId(productId).stream()
            .map(ProductApprovalRequestPersistenceMapper::toDomain)
            .toList();
    }

    @Override
    public List<ProductApprovalRequest> findByVendorId(Long vendorId) {
        return jpaRepository.findByVendorId(vendorId).stream()
            .map(ProductApprovalRequestPersistenceMapper::toDomain)
            .toList();
    }

    @Override
    public List<ProductApprovalRequest> findByStatus(ProductApprovalRequest.ApprovalStatus status) {
        return jpaRepository.findByStatus(status).stream()
            .map(ProductApprovalRequestPersistenceMapper::toDomain)
            .toList();
    }

    @Override
    public List<ProductApprovalRequest> findByProductIdAndStatus(Long productId, ProductApprovalRequest.ApprovalStatus status) {
        return jpaRepository.findByProductIdAndStatus(productId, status).stream()
            .map(ProductApprovalRequestPersistenceMapper::toDomain)
            .toList();
    }

    @Override
    public Optional<ProductApprovalRequest> findTopByProductIdAndStatusOrderByCreatedAtDesc(Long productId, ProductApprovalRequest.ApprovalStatus status) {
        return jpaRepository.findTopByProductIdAndStatusOrderByCreatedAtDesc(productId, status)
            .map(ProductApprovalRequestPersistenceMapper::toDomain);
    }

    @Override
    public long countByStatus(ProductApprovalRequest.ApprovalStatus status) {
        return jpaRepository.countByStatus(status);
    }
}