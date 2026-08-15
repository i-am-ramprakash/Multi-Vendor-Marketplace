package com.marketplace.vendor.infrastructure.persistence.repository;

import com.marketplace.vendor.domain.entity.Vendor;
import com.marketplace.vendor.domain.repository.VendorRepository;
import com.marketplace.vendor.domain.valueobject.StoreSlug;
import com.marketplace.vendor.domain.valueobject.VendorStatus;
import com.marketplace.vendor.infrastructure.persistence.entity.VendorJpaEntity;
import com.marketplace.vendor.infrastructure.persistence.mapper.VendorPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class VendorRepositoryImpl implements VendorRepository {

    private final VendorJpaRepository jpaRepository;

    @Override
    public Vendor save(Vendor vendor) {
        VendorJpaEntity jpaEntity = VendorPersistenceMapper.toJpaEntity(vendor);
        VendorJpaEntity saved = jpaRepository.save(jpaEntity);
        return VendorPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<Vendor> findById(Long id) {
        return jpaRepository.findById(id)
            .map(VendorPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Vendor> findByUserId(Long userId) {
        return jpaRepository.findByUserId(userId)
            .map(VendorPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Vendor> findByStoreSlug(StoreSlug storeSlug) {
        return jpaRepository.findByStoreSlug(storeSlug.getValue())
            .map(VendorPersistenceMapper::toDomain);
    }

    @Override
    public boolean existsByUserId(Long userId) {
        return jpaRepository.existsByUserId(userId);
    }

    @Override
    public boolean existsByStoreSlug(StoreSlug storeSlug) {
        return jpaRepository.existsByStoreSlug(storeSlug.getValue());
    }

    @Override
    public List<Vendor> findAll() {
        return jpaRepository.findAll().stream()
            .map(VendorPersistenceMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Vendor> findByStatus(VendorStatus status) {
        return jpaRepository.findByStatus(status).stream()
            .map(VendorPersistenceMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Vendor> findByStatusOrderByCreatedAtDesc(VendorStatus status) {
        return jpaRepository.findByStatusOrderByCreatedAtDesc(status).stream()
            .map(VendorPersistenceMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Vendor> findAllByOrderByCreatedAtDesc() {
        return jpaRepository.findAllByOrderByCreatedAtDesc().stream()
            .map(VendorPersistenceMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public long countByStatus(VendorStatus status) {
        return jpaRepository.countByStatus(status);
    }

    @Override
    public void delete(Vendor vendor) {
        jpaRepository.delete(VendorPersistenceMapper.toJpaEntity(vendor));
    }
}