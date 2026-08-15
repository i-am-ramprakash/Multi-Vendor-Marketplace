package com.marketplace.product.domain.repository;

import com.marketplace.product.domain.entity.ProductApprovalRequest;
import com.marketplace.product.domain.entity.ProductApprovalRequest.ApprovalStatus;

import java.util.List;
import java.util.Optional;

public interface ProductApprovalRequestRepository {

    ProductApprovalRequest save(ProductApprovalRequest request);

    Optional<ProductApprovalRequest> findById(Long id);

    List<ProductApprovalRequest> findByProductId(Long productId);

    List<ProductApprovalRequest> findByVendorId(Long vendorId);

    List<ProductApprovalRequest> findByStatus(ApprovalStatus status);

    List<ProductApprovalRequest> findByProductIdAndStatus(Long productId, ApprovalStatus status);

    Optional<ProductApprovalRequest> findTopByProductIdAndStatusOrderByCreatedAtDesc(Long productId, ApprovalStatus status);

    long countByStatus(ApprovalStatus status);
}