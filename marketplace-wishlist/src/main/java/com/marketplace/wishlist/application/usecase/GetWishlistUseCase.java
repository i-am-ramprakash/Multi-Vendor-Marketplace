package com.marketplace.wishlist.application.usecase;

import com.marketplace.wishlist.application.dto.WishlistResponse;
import com.marketplace.wishlist.domain.entity.Wishlist;
import com.marketplace.wishlist.domain.exception.WishlistNotFoundException;
import com.marketplace.wishlist.domain.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GetWishlistUseCase {

    private final WishlistRepository wishlistRepository;

    @Transactional(readOnly = true)
    public WishlistResponse execute(Long userId) {
        Wishlist wishlist = wishlistRepository.findByUserIdAndIsDefault(userId, true)
            .orElseThrow(() -> new WishlistNotFoundException("No default wishlist found for user: " + userId));

        return WishlistResponse.from(wishlist);
    }

    @Transactional(readOnly = true)
    public Optional<WishlistResponse> executeIfExists(Long userId) {
        return wishlistRepository.findByUserIdAndIsDefault(userId, true)
            .map(WishlistResponse::from);
    }
}