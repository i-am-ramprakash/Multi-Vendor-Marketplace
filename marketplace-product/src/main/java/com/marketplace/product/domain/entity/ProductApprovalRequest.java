package com.marketplace.product.domain.entity;

import com.marketplace.product.domain.valueobject.ProductStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductApprovalRequest {

    public enum RequestType {
        NEW_PRODUCT,
        RELIST,
        UPDATE
    }

    public enum ApprovalStatus {
        PENDING,
        APPROVED,
        REJECTED,
        CHANGES_REQUESTED
    }

    private Long id;
    private Product product;
    private Long vendorId;
    private RequestType requestType;
    private ApprovalStatus status;
    private String adminNotes;
    private String vendorNotes;
    private Long reviewedBy;
    private Instant reviewedAt;
    private Instant changesRequestedAt;
    private String changesRequestedReason;
    private Instant createdAt;
    private Instant updatedAt;

    public ProductApprovalRequest(Product product, Long vendorId, RequestType requestType) {
        this.product = product;
        this.vendorId = vendorId;
        this.requestType = requestType;
        this.status = ApprovalStatus.PENDING;
    }

    public void approve(Long reviewedBy, String adminNotes) {
        this.status = ApprovalStatus.APPROVED;
        this.reviewedBy = reviewedBy;
        this.reviewedAt = Instant.now();
        this.adminNotes = adminNotes;
    }

    public void reject(Long reviewedBy, String adminNotes) {
        this.status = ApprovalStatus.REJECTED;
        this.reviewedBy = reviewedBy;
        this.reviewedAt = Instant.now();
        this.adminNotes = adminNotes;
    }

    public void requestChanges(Long reviewedBy, String reason) {
        this.status = ApprovalStatus.CHANGES_REQUESTED;
        this.reviewedBy = reviewedBy;
        this.changesRequestedAt = Instant.now();
        this.changesRequestedReason = reason;
    }

    public void resubmit(String vendorNotes) {
        this.status = ApprovalStatus.PENDING;
        this.vendorNotes = vendorNotes;
        this.adminNotes = null;
        this.reviewedBy = null;
        this.reviewedAt = null;
    }

    public boolean isPending() {
        return this.status == ApprovalStatus.PENDING;
    }

    public boolean isApproved() {
        return this.status == ApprovalStatus.APPROVED;
    }

    public boolean isRejected() {
        return this.status == ApprovalStatus.REJECTED;
    }

    public boolean isChangesRequested() {
        return this.status == ApprovalStatus.CHANGES_REQUESTED;
    }
}
