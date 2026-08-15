package com.marketplace.vendor.domain.repository;

import com.marketplace.vendor.domain.entity.Vendor;
import com.marketplace.vendor.domain.valueobject.StoreSlug;
import com.marketplace.vendor.domain.valueobject.VendorStatus;

import java.util.List;
import java.util.Optional;

public interface VendorRepository {

    Vendor save(Vendor vendor);

    Optional<Vendor> findById(Long id);

    Optional<Vendor> findByUserId(Long userId);

    Optional<Vendor> findByStoreSlug(StoreSlug storeSlug);

    boolean existsByUserId(Long userId);

    boolean existsByStoreSlug(StoreSlug storeSlug);

    List<Vendor> findAll();

    List<Vendor> findByStatus(VendorStatus status);

    List<Vendor> findByStatusOrderByCreatedAtDesc(VendorStatus status);

    List<Vendor> findAllByOrderByCreatedAtDesc();

    long countByStatus(VendorStatus status);

    void delete(Vendor vendor);
}