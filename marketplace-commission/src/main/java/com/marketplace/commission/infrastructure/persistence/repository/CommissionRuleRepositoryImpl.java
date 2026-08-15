package com.marketplace.commission.infrastructure.persistence.repository;

import com.marketplace.commission.domain.entity.CommissionRule;
import com.marketplace.commission.domain.repository.CommissionRuleRepository;
import com.marketplace.commission.domain.valueobject.CommissionType;
import com.marketplace.commission.infrastructure.persistence.entity.CommissionRuleJpaEntity;
import com.marketplace.commission.infrastructure.persistence.mapper.CommissionRulePersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CommissionRuleRepositoryImpl implements CommissionRuleRepository {

    private final CommissionRuleJpaRepository jpaRepository;

    @Override
    public CommissionRule save(CommissionRule rule) {
        CommissionRuleJpaEntity jpa = CommissionRulePersistenceMapper.toJpaEntity(rule);
        CommissionRuleJpaEntity saved = jpaRepository.save(jpa);
        return CommissionRulePersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<CommissionRule> findById(Long id) {
        return jpaRepository.findById(id)
            .map(CommissionRulePersistenceMapper::toDomain);
    }

    @Override
    public List<CommissionRule> findAll() {
        return jpaRepository.findAll().stream()
            .map(CommissionRulePersistenceMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<CommissionRule> findByIsActiveTrue() {
        return jpaRepository.findByIsActiveTrue().stream()
            .map(CommissionRulePersistenceMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<CommissionRule> findByType(CommissionType type) {
        return jpaRepository.findByType(type).stream()
            .map(CommissionRulePersistenceMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<CommissionRule> findByVendorId(Long vendorId) {
        return jpaRepository.findByVendorId(vendorId).stream()
            .map(CommissionRulePersistenceMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<CommissionRule> findByCategoryId(Long categoryId) {
        return jpaRepository.findByCategoryId(categoryId).stream()
            .map(CommissionRulePersistenceMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<CommissionRule> findByVendorIdAndCategoryIdAndIsActiveTrue(Long vendorId, Long categoryId) {
        return jpaRepository.findByVendorIdAndCategoryIdAndIsActiveTrue(vendorId, categoryId)
            .map(CommissionRulePersistenceMapper::toDomain);
    }

    @Override
    public Optional<CommissionRule> findDefaultRule() {
        return jpaRepository.findDefaultRule()
            .map(CommissionRulePersistenceMapper::toDomain);
    }

    @Override
    public long countByIsActiveTrue() {
        return jpaRepository.countByIsActiveTrue();
    }
}