package com.marketplace.wishlist.infrastructure.persistence.repository;

import com.marketplace.wishlist.infrastructure.persistence.entity.WishlistItemJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistItemJpaRepository extends JpaRepository<WishlistItemJpaEntity, Long> {

    List<WishlistItemJpaEntity> findByWishlistId(Long wishlistId);

    Optional<WishlistItemJpaEntity> findByWishlistIdAndProductIdAndVariantId(Long wishlistId, Long productId, Long variantId);

    Optional<WishlistItemJpaEntity> findByWishlistIdAndProductId(Long wishlistId, Long productId);

    long countByWishlistId(Long wishlistId);

    void deleteByWishlistId(Long wishlistId);
}