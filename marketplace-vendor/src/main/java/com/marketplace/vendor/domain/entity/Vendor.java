package com.marketplace.vendor.domain.entity;

import com.marketplace.vendor.domain.valueobject.StoreSlug;
import com.marketplace.vendor.domain.valueobject.VendorStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vendor {

    private Long id;
    private Long userId;
    private String storeName;
    private StoreSlug storeSlug;
    private String storeDescription;
    private String storeLogoUrl;
    private String storeBannerUrl;
    private String contactEmail;
    private String contactPhone;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String taxId;
    private String bankAccountHolder;
    private String bankAccountNumber;
    private String bankName;
    private String bankRoutingNumber;
    private BigDecimal commissionRate;
    private VendorStatus status;
    private Instant approvedAt;
    private Long approvedBy;
    private String rejectionReason;
    private Instant suspendedAt;
    private Long suspendedBy;
    private String suspensionReason;
    private Integer totalProducts;
    private Integer totalOrders;
    private BigDecimal totalRevenue;
    private Instant createdAt;
    private Instant updatedAt;
    private Long version;

    public Vendor(Long userId, String storeName, StoreSlug storeSlug, String contactEmail, BigDecimal commissionRate) {
        this.userId = userId;
        this.storeName = storeName;
        this.storeSlug = storeSlug;
        this.contactEmail = contactEmail;
        this.commissionRate = commissionRate;
        this.status = VendorStatus.PENDING;
        this.totalProducts = 0;
        this.totalOrders = 0;
        this.totalRevenue = BigDecimal.ZERO;
    }

    public void approve(Long approvedBy) {
        if (!this.status.canTransitionTo(VendorStatus.APPROVED)) {
            throw new IllegalStateException("Cannot approve vendor in " + this.status + " status");
        }
        this.status = VendorStatus.APPROVED;
        this.approvedAt = Instant.now();
        this.approvedBy = approvedBy;
        this.rejectionReason = null;
    }

    public void reject(Long rejectedBy, String reason) {
        if (!this.status.canTransitionTo(VendorStatus.REJECTED)) {
            throw new IllegalStateException("Cannot reject vendor in " + this.status + " status");
        }
        this.status = VendorStatus.REJECTED;
        this.rejectionReason = reason;
    }

    public void suspend(Long suspendedBy, String reason) {
        if (!this.status.canTransitionTo(VendorStatus.SUSPENDED)) {
            throw new IllegalStateException("Cannot suspend vendor in " + this.status + " status");
        }
        this.status = VendorStatus.SUSPENDED;
        this.suspendedAt = Instant.now();
        this.suspendedBy = suspendedBy;
        this.suspensionReason = reason;
    }

    public void reactivate(Long reactivatedBy) {
        if (!this.status.canTransitionTo(VendorStatus.APPROVED)) {
            throw new IllegalStateException("Cannot reactivate vendor in " + this.status + " status");
        }
        this.status = VendorStatus.APPROVED;
        this.suspendedAt = null;
        this.suspendedBy = null;
        this.suspensionReason = null;
    }

    public void updateProfile(String storeName, String storeDescription, String storeLogoUrl,
                              String storeBannerUrl, String contactEmail, String contactPhone,
                              String addressLine1, String addressLine2, String city,
                              String state, String country, String postalCode) {
        if (storeName != null) this.storeName = storeName;
        if (storeDescription != null) this.storeDescription = storeDescription;
        if (storeLogoUrl != null) this.storeLogoUrl = storeLogoUrl;
        if (storeBannerUrl != null) this.storeBannerUrl = storeBannerUrl;
        if (contactEmail != null) this.contactEmail = contactEmail;
        if (contactPhone != null) this.contactPhone = contactPhone;
        if (addressLine1 != null) this.addressLine1 = addressLine1;
        if (addressLine2 != null) this.addressLine2 = addressLine2;
        if (city != null) this.city = city;
        if (state != null) this.state = state;
        if (country != null) this.country = country;
        if (postalCode != null) this.postalCode = postalCode;
    }

    public void updateCommissionRate(BigDecimal commissionRate) {
        if (commissionRate == null || commissionRate.compareTo(BigDecimal.ZERO) < 0 || commissionRate.compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException("Commission rate must be between 0 and 100");
        }
        this.commissionRate = commissionRate;
    }

    public void incrementProductCount() {
        this.totalProducts++;
    }

    public void decrementProductCount() {
        if (this.totalProducts > 0) {
            this.totalProducts--;
        }
    }

    public void addOrder(BigDecimal orderAmount) {
        this.totalOrders++;
        this.totalRevenue = this.totalRevenue.add(orderAmount);
    }

    public boolean isOwner(Long userId) {
        return this.userId.equals(userId);
    }

    public boolean isApproved() {
        return this.status == VendorStatus.APPROVED;
    }

    public boolean isPending() {
        return this.status == VendorStatus.PENDING;
    }

    public String getFullAddress() {
        StringBuilder sb = new StringBuilder();
        if (addressLine1 != null && !addressLine1.isEmpty()) {
            sb.append(addressLine1);
        }
        if (addressLine2 != null && !addressLine2.isEmpty()) {
            if (!sb.isEmpty()) sb.append(", ");
            sb.append(addressLine2);
        }
        if (city != null && !city.isEmpty()) {
            if (!sb.isEmpty()) sb.append(", ");
            sb.append(city);
        }
        if (state != null && !state.isEmpty()) {
            if (!sb.isEmpty()) sb.append(", ");
            sb.append(state);
        }
        if (country != null && !country.isEmpty()) {
            if (!sb.isEmpty()) sb.append(", ");
            sb.append(country);
        }
        if (postalCode != null && !postalCode.isEmpty()) {
            if (!sb.isEmpty()) sb.append(" ");
            sb.append(postalCode);
        }
        return sb.toString();
    }
}