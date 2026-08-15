package com.marketplace.cart.infrastructure.persistence.repository;

import com.marketplace.cart.infrastructure.persistence.entity.CartItemJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemJpaRepository extends JpaRepository<CartItemJpaEntity, Long> {

    List<CartItemJpaEntity> findByCartId(Long cartId);

    Optional<CartItemJpaEntity> findByCartIdAndProductIdAndVariantId(Long cartId, Long productId, Long variantId);

    Optional<CartItemJpaEntity> findByCartIdAndProductId(Long cartId, Long productId);

    long countByCartId(Long cartId);

    void deleteByCartId(Long cartId);
}