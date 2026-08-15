package com.marketplace.order.infrastructure.persistence.repository;

import com.marketplace.order.domain.valueobject.OrderStatus;
import com.marketplace.order.infrastructure.persistence.entity.OrderJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderJpaRepository extends JpaRepository<OrderJpaEntity, Long> {

    Optional<OrderJpaEntity> findByOrderNumber(String orderNumber);

    List<OrderJpaEntity> findByUserId(Long userId);

    Page<OrderJpaEntity> findByUserId(Long userId, Pageable pageable);

    List<OrderJpaEntity> findByUserIdAndStatus(Long userId, OrderStatus status);

    Page<OrderJpaEntity> findByUserIdAndStatus(Long userId, OrderStatus status, Pageable pageable);

    List<OrderJpaEntity> findByStatus(OrderStatus status);

    Page<OrderJpaEntity> findByStatus(OrderStatus status, Pageable pageable);

    long countByUserId(Long userId);

    long countByUserIdAndStatus(Long userId, OrderStatus status);

    long countByStatus(OrderStatus status);

    @Query("SELECT o FROM OrderJpaEntity o JOIN o.items i WHERE " +
           "(:keyword IS NULL OR LOWER(o.orderNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:userId IS NULL OR o.userId = :userId) AND " +
           "(:vendorId IS NULL OR i.vendorId = :vendorId) AND " +
           "(:status IS NULL OR o.status = :status)")
    Page<OrderJpaEntity> search(
        @Param("keyword") String keyword,
        @Param("userId") Long userId,
        @Param("vendorId") Long vendorId,
        @Param("status") OrderStatus status,
        Pageable pageable
    );
}