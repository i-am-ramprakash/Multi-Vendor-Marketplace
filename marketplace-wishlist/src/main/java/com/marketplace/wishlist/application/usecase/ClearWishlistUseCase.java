package com.marketplace.wishlist.application.usecase;

import com.marketplace.wishlist.domain.entity.Wishlist;
import com.marketplace.wishlist.domain.exception.WishlistNotFoundException;
import com.marketplace.wishlist.domain.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ClearWishlistUseCase {

    private final WishlistRepository wishlistRepository;

    @Transactional
    public void execute(Long userId) {
        Wishlist wishlist = wishlistRepository.findByUserIdAndIsDefault(userId, true)
            .orElseThrow(() -> new WishlistNotFoundException("No default wishlist found for user: " + userId));

        wishlist.clear();
        wishlistRepository.save(wishlist);
    }
}