package com.marketplace.admin.infrastructure.persistence.repository;

import com.marketplace.admin.infrastructure.persistence.entity.VendorJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface VendorJpaRepository extends JpaRepository<VendorJpaEntity, Long> {

    @Query("SELECT COUNT(v) FROM VendorJpaEntity v")
    Long countAllVendors();

    @Query("SELECT COUNT(v) FROM VendorJpaEntity v WHERE v.status = :status")
    Long countByStatus(@Param("status") String status);

    @Query("SELECT COUNT(v) FROM VendorJpaEntity v WHERE v.createdAt BETWEEN :from AND :to")
    Long countByCreatedAtBetween(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);
}
