package com.marketplace.commission.infrastructure.persistence.mapper;

import com.marketplace.commission.domain.entity.CommissionRule;
import com.marketplace.commission.infrastructure.persistence.entity.CommissionRuleJpaEntity;

import java.lang.reflect.Field;

public final class CommissionRulePersistenceMapper {

    private CommissionRulePersistenceMapper() {}

    public static CommissionRuleJpaEntity toJpaEntity(CommissionRule domain) {
        if (domain == null) return null;

        CommissionRuleJpaEntity jpa = new CommissionRuleJpaEntity();
        jpa.setId(domain.getId());
        jpa.setName(domain.getName());
        jpa.setDescription(domain.getDescription());
        jpa.setType(domain.getType());
        jpa.setRate(domain.getRate());
        jpa.setFixedAmount(domain.getFixedAmount());
        jpa.setMinOrderAmount(domain.getMinOrderAmount());
        jpa.setMaxCommissionAmount(domain.getMaxCommissionAmount());
        jpa.setCategoryId(domain.getCategoryId());
        jpa.setVendorId(domain.getVendorId());
        jpa.setDefault(domain.isDefault());
        jpa.setActive(domain.isActive());
        jpa.setPriority(domain.getPriority());
        jpa.setEffectiveFrom(domain.getEffectiveFrom());
        jpa.setEffectiveTo(domain.getEffectiveTo());
        jpa.setCreatedAt(domain.getCreatedAt());
        jpa.setUpdatedAt(domain.getUpdatedAt());

        return jpa;
    }

    public static CommissionRule toDomain(CommissionRuleJpaEntity jpa) {
        if (jpa == null) return null;

        CommissionRule rule = new CommissionRule(jpa.getName(), jpa.getDescription(), jpa.getType(), jpa.getRate());
        setId(rule, jpa.getId());
        rule.setFixedAmount(jpa.getFixedAmount());
        rule.setMinOrderAmount(jpa.getMinOrderAmount());
        rule.setMaxCommissionAmount(jpa.getMaxCommissionAmount());
        rule.setCategoryId(jpa.getCategoryId());
        rule.setVendorId(jpa.getVendorId());
        rule.setDefault(jpa.isDefault());
        rule.setActive(jpa.isActive());
        rule.setPriority(jpa.getPriority());
        rule.setEffectiveFrom(jpa.getEffectiveFrom());
        rule.setEffectiveTo(jpa.getEffectiveTo());
        rule.setCreatedAt(jpa.getCreatedAt());
        rule.setUpdatedAt(jpa.getUpdatedAt());

        return rule;
    }

    private static void setId(CommissionRule rule, Long id) {
        try {
            Field field = CommissionRule.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(rule, id);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot set commission rule ID", e);
        }
    }
}