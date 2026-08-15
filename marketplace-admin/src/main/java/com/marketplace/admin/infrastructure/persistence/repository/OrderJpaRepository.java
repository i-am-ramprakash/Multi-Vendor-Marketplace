package com.marketplace.admin.infrastructure.persistence.repository;

import com.marketplace.admin.infrastructure.persistence.entity.OrderJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository
public interface OrderJpaRepository extends JpaRepository<OrderJpaEntity, Long> {

    @Query("SELECT COUNT(o) FROM OrderJpaEntity o")
    Long countAllOrders();

    @Query("SELECT COUNT(o) FROM OrderJpaEntity o WHERE o.status = :status")
    Long countByStatus(@Param("status") String status);

    @Query("SELECT COALESCE(SUM(o.total), 0) FROM OrderJpaEntity o WHERE o.status = 'DELIVERED'")
    BigDecimal sumTotalRevenue();

    @Query("SELECT COALESCE(AVG(o.total), 0) FROM OrderJpaEntity o WHERE o.status = 'DELIVERED'")
    BigDecimal getAverageOrderValue();

    @Query("SELECT COUNT(o) FROM OrderJpaEntity o WHERE o.createdAt BETWEEN :from AND :to")
    Long countByCreatedAtBetween(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query("SELECT COALESCE(SUM(o.total), 0) FROM OrderJpaEntity o WHERE o.status = 'DELIVERED' AND o.createdAt BETWEEN :from AND :to")
    BigDecimal sumRevenueByDateRange(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);
}