package com.marketplace.wishlist.infrastructure.persistence.repository;

import com.marketplace.wishlist.infrastructure.persistence.entity.WishlistJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistJpaRepository extends JpaRepository<WishlistJpaEntity, Long> {

    Optional<WishlistJpaEntity> findByUserId(Long userId);

    Optional<WishlistJpaEntity> findByUserIdAndIsDefault(Long userId, Boolean isDefault);

    List<WishlistJpaEntity> findByUserIdOrderByCreatedAtDesc(Long userId);

    boolean existsByUserIdAndName(Long userId, String name);

    long countByUserId(Long userId);
}