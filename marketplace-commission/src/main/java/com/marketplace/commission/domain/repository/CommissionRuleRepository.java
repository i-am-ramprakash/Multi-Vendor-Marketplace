package com.marketplace.commission.domain.repository;

import com.marketplace.commission.domain.entity.CommissionRule;
import com.marketplace.commission.domain.valueobject.CommissionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CommissionRuleRepository {

    CommissionRule save(CommissionRule rule);

    Optional<CommissionRule> findById(Long id);

    List<CommissionRule> findAll();

    List<CommissionRule> findByIsActiveTrue();

    List<CommissionRule> findByType(CommissionType type);

    List<CommissionRule> findByVendorId(Long vendorId);

    List<CommissionRule> findByCategoryId(Long categoryId);

    Optional<CommissionRule> findByVendorIdAndCategoryIdAndIsActiveTrue(Long vendorId, Long categoryId);

    Optional<CommissionRule> findDefaultRule();

    long countByIsActiveTrue();
}