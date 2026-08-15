package com.marketplace.wishlist.application.usecase;

import com.marketplace.wishlist.domain.entity.Wishlist;
import com.marketplace.wishlist.domain.entity.WishlistItem;
import com.marketplace.wishlist.domain.event.ItemRemovedFromWishlistEvent;
import com.marketplace.wishlist.domain.exception.WishlistNotFoundException;
import com.marketplace.wishlist.domain.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RemoveFromWishlistUseCase {

    private final WishlistRepository wishlistRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void execute(Long userId, Long productId, Long variantId) {
        Wishlist wishlist = wishlistRepository.findByUserIdAndIsDefault(userId, true)
            .orElseThrow(() -> new WishlistNotFoundException("No default wishlist found for user: " + userId));

        Long wishlistId = wishlist.getId();
        wishlist.removeItem(productId, variantId);
        wishlistRepository.save(wishlist);

        eventPublisher.publishEvent(new ItemRemovedFromWishlistEvent(
            this,
            wishlistId,
            userId,
            productId,
            variantId
        ));
    }
}