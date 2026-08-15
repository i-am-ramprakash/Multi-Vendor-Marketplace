package com.marketplace.vendor.infrastructure.persistence.repository;

import com.marketplace.vendor.infrastructure.persistence.entity.VendorJpaEntity;
import com.marketplace.vendor.domain.valueobject.VendorStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VendorJpaRepository extends JpaRepository<VendorJpaEntity, Long> {

    Optional<VendorJpaEntity> findByUserId(Long userId);

    Optional<VendorJpaEntity> findByStoreSlug(String storeSlug);

    boolean existsByUserId(Long userId);

    boolean existsByStoreSlug(String storeSlug);

    List<VendorJpaEntity> findByStatus(VendorStatus status);

    List<VendorJpaEntity> findByStatusOrderByCreatedAtDesc(VendorStatus status);

    List<VendorJpaEntity> findAllByOrderByCreatedAtDesc();

    long countByStatus(VendorStatus status);
}