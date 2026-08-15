package com.marketplace.wishlist.domain.repository;

import com.marketplace.wishlist.domain.entity.Wishlist;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository {

    Wishlist save(Wishlist wishlist);

    Optional<Wishlist> findById(Long id);

    Optional<Wishlist> findByUserId(Long userId);

    Optional<Wishlist> findByUserIdAndIsDefault(Long userId, boolean isDefault);

    List<Wishlist> findByUserIdOrderByCreatedAtDesc(Long userId);

    boolean existsByUserIdAndName(Long userId, String name);

    long countByUserId(Long userId);

    void delete(Wishlist wishlist);
}