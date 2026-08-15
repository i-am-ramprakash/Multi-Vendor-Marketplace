package com.marketplace.cart.infrastructure.persistence.repository;

import com.marketplace.cart.domain.valueobject.CartStatus;
import com.marketplace.cart.infrastructure.persistence.entity.CartJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartJpaRepository extends JpaRepository<CartJpaEntity, Long> {

    Optional<CartJpaEntity> findByUserId(Long userId);

    Optional<CartJpaEntity> findByUserIdAndStatus(Long userId, CartStatus status);

    Optional<CartJpaEntity> findBySessionId(String sessionId);

    List<CartJpaEntity> findByUserIdAndStatusIn(Long userId, List<CartStatus> statuses);

    long countByUserIdAndStatus(Long userId, CartStatus status);

    void deleteByStatus(CartStatus status);
}