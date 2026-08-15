package com.marketplace.product.infrastructure.persistence.repository;

import com.marketplace.product.domain.entity.ProductApprovalRequest;
import com.marketplace.product.infrastructure.persistence.entity.ProductApprovalRequestJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductApprovalRequestJpaRepository extends JpaRepository<ProductApprovalRequestJpaEntity, Long> {

    List<ProductApprovalRequestJpaEntity> findByProductId(Long productId);

    List<ProductApprovalRequestJpaEntity> findByVendorId(Long vendorId);

    List<ProductApprovalRequestJpaEntity> findByStatus(ProductApprovalRequest.ApprovalStatus status);

    List<ProductApprovalRequestJpaEntity> findByProductIdAndStatus(Long productId, ProductApprovalRequest.ApprovalStatus status);

    Optional<ProductApprovalRequestJpaEntity> findTopByProductIdAndStatusOrderByCreatedAtDesc(Long productId, ProductApprovalRequest.ApprovalStatus status);

    long countByStatus(ProductApprovalRequest.ApprovalStatus status);
}