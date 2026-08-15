package com.marketplace.vendor.infrastructure.persistence.mapper;

import com.marketplace.vendor.domain.entity.Vendor;
import com.marketplace.vendor.domain.valueobject.StoreSlug;
import com.marketplace.vendor.infrastructure.persistence.entity.VendorJpaEntity;

import java.lang.reflect.Field;

public final class VendorPersistenceMapper {

    private VendorPersistenceMapper() {}

    public static VendorJpaEntity toJpaEntity(Vendor domain) {
        if (domain == null) return null;

        VendorJpaEntity jpa = new VendorJpaEntity();
        jpa.setId(domain.getId());
        jpa.setUserId(domain.getUserId());
        jpa.setStoreName(domain.getStoreName());
        jpa.setStoreSlug(domain.getStoreSlug() != null ? domain.getStoreSlug().getValue() : null);
        jpa.setStoreDescription(domain.getStoreDescription());
        jpa.setStoreLogoUrl(domain.getStoreLogoUrl());
        jpa.setStoreBannerUrl(domain.getStoreBannerUrl());
        jpa.setContactEmail(domain.getContactEmail());
        jpa.setContactPhone(domain.getContactPhone());
        jpa.setAddressLine1(domain.getAddressLine1());
        jpa.setAddressLine2(domain.getAddressLine2());
        jpa.setCity(domain.getCity());
        jpa.setState(domain.getState());
        jpa.setCountry(domain.getCountry());
        jpa.setPostalCode(domain.getPostalCode());
        jpa.setTaxId(domain.getTaxId());
        jpa.setBankAccountHolder(domain.getBankAccountHolder());
        jpa.setBankAccountNumber(domain.getBankAccountNumber());
        jpa.setBankName(domain.getBankName());
        jpa.setBankRoutingNumber(domain.getBankRoutingNumber());
        jpa.setCommissionRate(domain.getCommissionRate());
        jpa.setStatus(domain.getStatus());
        jpa.setApprovedAt(domain.getApprovedAt());
        jpa.setApprovedBy(domain.getApprovedBy());
        jpa.setRejectionReason(domain.getRejectionReason());
        jpa.setSuspendedAt(domain.getSuspendedAt());
        jpa.setSuspendedBy(domain.getSuspendedBy());
        jpa.setSuspensionReason(domain.getSuspensionReason());
        jpa.setTotalProducts(domain.getTotalProducts());
        jpa.setTotalOrders(domain.getTotalOrders());
        jpa.setTotalRevenue(domain.getTotalRevenue());
        jpa.setCreatedAt(domain.getCreatedAt());
        jpa.setUpdatedAt(domain.getUpdatedAt());
        jpa.setVersion(domain.getVersion());
        return jpa;
    }

    public static Vendor toDomain(VendorJpaEntity jpa) {
        if (jpa == null) return null;

        Vendor vendor = new Vendor(
            jpa.getUserId(),
            jpa.getStoreName(),
            jpa.getStoreSlug() != null ? StoreSlug.of(jpa.getStoreSlug()) : null,
            jpa.getContactEmail(),
            jpa.getCommissionRate()
        );

        setId(vendor, jpa.getId());
        vendor.setStatus(jpa.getStatus());
        vendor.setStoreDescription(jpa.getStoreDescription());
        vendor.setStoreLogoUrl(jpa.getStoreLogoUrl());
        vendor.setStoreBannerUrl(jpa.getStoreBannerUrl());
        vendor.setContactPhone(jpa.getContactPhone());
        vendor.setAddressLine1(jpa.getAddressLine1());
        vendor.setAddressLine2(jpa.getAddressLine2());
        vendor.setCity(jpa.getCity());
        vendor.setState(jpa.getState());
        vendor.setCountry(jpa.getCountry());
        vendor.setPostalCode(jpa.getPostalCode());
        vendor.setTaxId(jpa.getTaxId());
        vendor.setBankAccountHolder(jpa.getBankAccountHolder());
        vendor.setBankAccountNumber(jpa.getBankAccountNumber());
        vendor.setBankName(jpa.getBankName());
        vendor.setBankRoutingNumber(jpa.getBankRoutingNumber());
        vendor.setApprovedAt(jpa.getApprovedAt());
        vendor.setApprovedBy(jpa.getApprovedBy());
        vendor.setRejectionReason(jpa.getRejectionReason());
        vendor.setSuspendedAt(jpa.getSuspendedAt());
        vendor.setSuspendedBy(jpa.getSuspendedBy());
        vendor.setSuspensionReason(jpa.getSuspensionReason());
        vendor.setTotalProducts(jpa.getTotalProducts());
        vendor.setTotalOrders(jpa.getTotalOrders());
        vendor.setTotalRevenue(jpa.getTotalRevenue());
        vendor.setCreatedAt(jpa.getCreatedAt());
        vendor.setUpdatedAt(jpa.getUpdatedAt());
        vendor.setVersion(jpa.getVersion());

        return vendor;
    }

    private static void setId(Vendor vendor, Long id) {
        try {
            Field field = Vendor.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(vendor, id);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot set vendor ID", e);
        }
    }
}