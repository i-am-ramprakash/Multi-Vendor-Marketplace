package com.marketplace.admin.infrastructure.persistence.repository;

import com.marketplace.admin.infrastructure.persistence.entity.ProductJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository
public interface ProductJpaRepository extends JpaRepository<ProductJpaEntity, Long> {

    @Query("SELECT COUNT(p) FROM ProductJpaEntity p")
    Long countAllProducts();

    @Query("SELECT COUNT(p) FROM ProductJpaEntity p WHERE p.status = :status")
    Long countByStatus(@Param("status") String status);

    @Query("SELECT COUNT(p) FROM ProductJpaEntity p WHERE p.createdAt BETWEEN :from AND :to")
    Long countByCreatedAtBetween(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query("SELECT COALESCE(AVG(p.basePrice), 0) FROM ProductJpaEntity p WHERE p.status = 'APPROVED'")
    BigDecimal getAverageProductPrice();

    @Query("SELECT COALESCE(SUM(p.totalSold), 0) FROM ProductJpaEntity p")
    Long getTotalUnitsSold();
}