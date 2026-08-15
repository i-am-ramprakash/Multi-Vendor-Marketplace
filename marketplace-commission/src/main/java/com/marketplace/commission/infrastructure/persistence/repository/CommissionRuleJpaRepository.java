package com.marketplace.commission.infrastructure.persistence.repository;

import com.marketplace.commission.domain.valueobject.CommissionType;
import com.marketplace.commission.infrastructure.persistence.entity.CommissionRuleJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommissionRuleJpaRepository extends JpaRepository<CommissionRuleJpaEntity, Long> {

    List<CommissionRuleJpaEntity> findByIsActiveTrue();

    List<CommissionRuleJpaEntity> findByType(CommissionType type);

    List<CommissionRuleJpaEntity> findByVendorId(Long vendorId);

    List<CommissionRuleJpaEntity> findByCategoryId(Long categoryId);

    Optional<CommissionRuleJpaEntity> findByVendorIdAndCategoryIdAndIsActiveTrue(Long vendorId, Long categoryId);

    @Query("SELECT r FROM CommissionRuleJpaEntity r WHERE r.isDefault = true AND r.isActive = true")
    Optional<CommissionRuleJpaEntity> findDefaultRule();

    long countByIsActiveTrue();

    @Query("SELECT r FROM CommissionRuleJpaEntity r WHERE r.isActive = true AND (r.vendorId IS NULL OR r.vendorId = :vendorId) AND (r.categoryId IS NULL OR r.categoryId = :categoryId) ORDER BY r.priority DESC, r.vendorId ASC NULLS LAST, r.categoryId ASC NULLS LAST")
    List<CommissionRuleJpaEntity> findApplicableRules(@Param("vendorId") Long vendorId, @Param("categoryId") Long categoryId);
}