package com.marketplace.product.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductRelistRequest {

    public enum RelistStatus {
        PENDING,
        APPROVED,
        REJECTED
    }

    private Long id;
    private Product product;
    private Long vendorId;
    private RelistStatus status;
    private String reason;
    private String adminNotes;
    private Long reviewedBy;
    private Instant reviewedAt;
    private Instant createdAt;
    private Instant updatedAt;

    public ProductRelistRequest(Product product, Long vendorId, String reason) {
        this.product = product;
        this.vendorId = vendorId;
        this.reason = reason;
        this.status = RelistStatus.PENDING;
    }

    public void approve(Long reviewedBy, String adminNotes) {
        this.status = RelistStatus.APPROVED;
        this.reviewedBy = reviewedBy;
        this.reviewedAt = Instant.now();
        this.adminNotes = adminNotes;
    }

    public void reject(Long reviewedBy, String adminNotes) {
        this.status = RelistStatus.REJECTED;
        this.reviewedBy = reviewedBy;
        this.reviewedAt = Instant.now();
        this.adminNotes = adminNotes;
    }

    public boolean isPending() {
        return this.status == RelistStatus.PENDING;
    }
}
