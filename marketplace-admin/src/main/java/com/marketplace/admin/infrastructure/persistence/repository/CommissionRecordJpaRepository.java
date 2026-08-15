package com.marketplace.admin.infrastructure.persistence.repository;

import com.marketplace.admin.infrastructure.persistence.entity.CommissionRecordJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository
public interface CommissionRecordJpaRepository extends JpaRepository<CommissionRecordJpaEntity, Long> {

    @Query("SELECT COALESCE(SUM(c.commissionAmount), 0) FROM CommissionRecordJpaEntity c WHERE c.isSettled = TRUE")
    BigDecimal sumTotalCommission();

    @Query("SELECT COALESCE(SUM(c.commissionAmount), 0) FROM CommissionRecordJpaEntity c WHERE c.isSettled = TRUE AND c.createdAt BETWEEN :from AND :to")
    BigDecimal sumCommissionByDateRange(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);
}