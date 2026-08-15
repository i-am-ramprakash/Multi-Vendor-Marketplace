package com.marketplace.product.infrastructure.persistence.mapper;

import com.marketplace.product.domain.entity.ProductApprovalRequest;
import com.marketplace.product.infrastructure.persistence.entity.ProductApprovalRequestJpaEntity;

import java.lang.reflect.Field;

public final class ProductApprovalRequestPersistenceMapper {

    private ProductApprovalRequestPersistenceMapper() {}

    public static ProductApprovalRequestJpaEntity toJpaEntity(ProductApprovalRequest domain) {
        if (domain == null) return null;

        ProductApprovalRequestJpaEntity jpa = new ProductApprovalRequestJpaEntity();
        jpa.setId(domain.getId());
        jpa.setVendorId(domain.getVendorId());
        jpa.setRequestType(domain.getRequestType());
        jpa.setStatus(domain.getStatus());
        jpa.setAdminNotes(domain.getAdminNotes());
        jpa.setVendorNotes(domain.getVendorNotes());
        jpa.setReviewedBy(domain.getReviewedBy());
        jpa.setReviewedAt(domain.getReviewedAt());
        jpa.setChangesRequestedAt(domain.getChangesRequestedAt());
        jpa.setChangesRequestedReason(domain.getChangesRequestedReason());
        jpa.setCreatedAt(domain.getCreatedAt());
        jpa.setUpdatedAt(domain.getUpdatedAt());

        if (domain.getProduct() != null) {
            com.marketplace.product.infrastructure.persistence.entity.ProductJpaEntity productJpa = new com.marketplace.product.infrastructure.persistence.entity.ProductJpaEntity();
            productJpa.setId(domain.getProduct().getId());
            jpa.setProduct(productJpa);
        }

        return jpa;
    }

    public static ProductApprovalRequest toDomain(ProductApprovalRequestJpaEntity jpa) {
        if (jpa == null) return null;

        ProductApprovalRequest request = new ProductApprovalRequest(
            null,
            jpa.getVendorId(),
            jpa.getRequestType()
        );

        setId(request, jpa.getId());
        request.setStatus(jpa.getStatus());
        request.setAdminNotes(jpa.getAdminNotes());
        request.setVendorNotes(jpa.getVendorNotes());
        request.setReviewedBy(jpa.getReviewedBy());
        request.setReviewedAt(jpa.getReviewedAt());
        request.setChangesRequestedAt(jpa.getChangesRequestedAt());
        request.setChangesRequestedReason(jpa.getChangesRequestedReason());
        request.setCreatedAt(jpa.getCreatedAt());
        request.setUpdatedAt(jpa.getUpdatedAt());

        return request;
    }

    private static void setId(ProductApprovalRequest request, Long id) {
        try {
            Field field = ProductApprovalRequest.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(request, id);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot set approval request ID", e);
        }
    }
}